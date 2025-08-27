package com.admin.identity.service.impl;

import cn.hutool.core.util.StrUtil;
import com.admin.common.core.exception.ServiceException;
import com.admin.identity.domain.dto.*;
import com.admin.identity.domain.entity.Tenant;
import com.admin.identity.mapper.TenantMapper;
import com.admin.identity.service.TenantService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 租户服务实现
 *
 * @author admin
 * @since 2024-01-15
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements TenantService {

    @Override
    public Long createTenant(TenantCreateDTO createDTO) {
        log.info("开始创建租户：{}", createDTO.getName());
        
        // 1. 验证租户编码唯一性
        if (existsByCode(createDTO.getCode())) {
            throw new ServiceException("租户编码已存在：" + createDTO.getCode());
        }
        
        // 2. 验证域名唯一性（如果有）
        if (StrUtil.isNotBlank(createDTO.getDomain()) && existsByDomain(createDTO.getDomain())) {
            throw new ServiceException("域名已被使用：" + createDTO.getDomain());
        }
        
        // 3. 创建租户实体
        Tenant tenant = new Tenant();
        BeanUtils.copyProperties(createDTO, tenant);
        
        // 4. 设置默认值
        setTenantDefaults(tenant, createDTO);
        
        // 5. 计算层级和祖级信息
        calculateTenantHierarchy(tenant, createDTO.getParentId());
        
        // 6. 保存租户
        boolean result = save(tenant);
        if (!result) {
            throw new ServiceException("创建租户失败");
        }
        
        // 7. 初始化租户数据
        initializeTenantData(tenant);
        
        log.info("租户创建成功，ID：{}，编码：{}", tenant.getId(), tenant.getCode());
        return tenant.getId();
    }

    @Override
    public Boolean updateTenant(TenantUpdateDTO updateDTO) {
        log.info("开始更新租户：{}", updateDTO.getId());
        
        // 1. 检查租户是否存在
        Tenant existingTenant = getById(updateDTO.getId());
        if (existingTenant == null) {
            throw new ServiceException("租户不存在");
        }
        
        // 2. 验证域名唯一性（如果有更新）
        if (StrUtil.isNotBlank(updateDTO.getDomain()) && 
            existsByDomainExcludeId(updateDTO.getDomain(), updateDTO.getId())) {
            throw new ServiceException("域名已被使用：" + updateDTO.getDomain());
        }
        
        // 3. 更新租户信息
        Tenant tenant = new Tenant();
        BeanUtils.copyProperties(updateDTO, tenant);
        tenant.setUpdateTime(LocalDateTime.now());
        
        // 4. 执行更新
        boolean result = updateById(tenant);
        if (!result) {
            throw new ServiceException("更新租户失败");
        }
        
        log.info("租户更新成功：{}", updateDTO.getId());
        return true;
    }

    @Override
    public Boolean deleteTenant(Long id) {
        log.info("开始删除租户：{}", id);
        
        // 1. 检查租户是否存在
        Tenant tenant = getById(id);
        if (tenant == null) {
            throw new ServiceException("租户不存在");
        }
        
        // 2. 检查是否有子租户
        List<TenantResponseDTO> subTenants = getSubTenants(id);
        if (!subTenants.isEmpty()) {
            throw new ServiceException("存在子租户，无法删除");
        }
        
        // 3. 检查是否有用户
        Integer userCount = baseMapper.countUsersByTenantId(id);
        if (userCount > 0) {
            throw new ServiceException("租户下存在用户，无法删除");
        }
        
        // 4. 逻辑删除
        boolean result = removeById(id);
        if (!result) {
            throw new ServiceException("删除租户失败");
        }
        
        log.info("租户删除成功：{}", id);
        return true;
    }

    @Override
    public Boolean batchDeleteTenants(List<Long> ids) {
        log.info("开始批量删除租户：{}", ids);
        
        for (Long id : ids) {
            deleteTenant(id);
        }
        
        log.info("批量删除租户成功：{}", ids);
        return true;
    }

    @Override
    public IPage<TenantResponseDTO> getTenantPage(TenantQueryDTO queryDTO) {
        Page<TenantResponseDTO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        return baseMapper.selectTenantPage(page, queryDTO);
    }

    @Override
    public TenantResponseDTO getTenantDetail(Long id) {
        TenantResponseDTO detail = baseMapper.selectTenantDetailById(id);
        if (detail == null) {
            throw new ServiceException("租户不存在");
        }
        
        // 计算剩余天数
        if (detail.getExpireTime() != null) {
            long days = ChronoUnit.DAYS.between(LocalDateTime.now(), detail.getExpireTime());
            detail.setRemainingDays(Math.max(0, days));
        }
        
        return detail;
    }

    @Override
    public List<TenantResponseDTO> getSubTenants(Long parentId) {
        return baseMapper.selectSubTenants(parentId);
    }

    @Override
    public Boolean activateTenant(Long id) {
        return updateTenantStatus(id, Tenant.TenantStatus.ACTIVE.name(), "租户激活");
    }

    @Override
    public Boolean suspendTenant(Long id, String reason) {
        return updateTenantStatus(id, Tenant.TenantStatus.SUSPENDED.name(), reason);
    }

    @Override
    public Boolean resumeTenant(Long id) {
        return updateTenantStatus(id, Tenant.TenantStatus.ACTIVE.name(), "租户恢复");
    }

    @Override
    public Boolean renewTenant(Long id, Integer months) {
        log.info("开始续费租户：{}，续费月数：{}", id, months);
        
        Tenant tenant = getById(id);
        if (tenant == null) {
            throw new ServiceException("租户不存在");
        }
        
        // 计算新的过期时间
        LocalDateTime currentExpireTime = tenant.getExpireTime();
        LocalDateTime newExpireTime;
        
        if (currentExpireTime == null || currentExpireTime.isBefore(LocalDateTime.now())) {
            // 如果已过期或没有过期时间，从当前时间开始计算
            newExpireTime = LocalDateTime.now().plusMonths(months);
        } else {
            // 如果未过期，从当前过期时间延长
            newExpireTime = currentExpireTime.plusMonths(months);
        }
        
        // 更新过期时间和状态
        LambdaUpdateWrapper<Tenant> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Tenant::getId, id)
                    .set(Tenant::getExpireTime, newExpireTime)
                    .set(Tenant::getStatus, Tenant.TenantStatus.ACTIVE.name())
                    .set(Tenant::getUpdateTime, LocalDateTime.now());
        
        boolean result = update(updateWrapper);
        if (!result) {
            throw new ServiceException("租户续费失败");
        }
        
        log.info("租户续费成功：{}，新过期时间：{}", id, newExpireTime);
        return true;
    }

    @Override
    public Boolean existsByCode(String code) {
        return baseMapper.countByCode(code) > 0;
    }

    @Override
    public Boolean existsByCodeExcludeId(String code, Long excludeId) {
        return baseMapper.countByCodeExcludeId(code, excludeId) > 0;
    }

    @Override
    public Boolean existsByDomain(String domain) {
        return baseMapper.countByDomain(domain) > 0;
    }

    @Override
    public Boolean existsByDomainExcludeId(String domain, Long excludeId) {
        return baseMapper.countByDomainExcludeId(domain, excludeId) > 0;
    }

    @Override
    public TenantQuotaDTO getTenantQuota(Long tenantId) {
        Tenant tenant = getById(tenantId);
        if (tenant == null) {
            throw new ServiceException("租户不存在");
        }
        
        TenantQuotaDTO quota = new TenantQuotaDTO();
        quota.setTenantId(tenantId);
        quota.setTenantName(tenant.getName());
        quota.setMaxUsers(tenant.getMaxUsers());
        quota.setMaxStorage(tenant.getMaxStorage());
        quota.setMaxDepartments(tenant.getMaxDepartments());
        quota.setMaxRoles(tenant.getMaxRoles());
        
        // 查询当前使用量
        quota.setCurrentUsers(baseMapper.countUsersByTenantId(tenantId));
        quota.setCurrentDepartments(baseMapper.countDepartmentsByTenantId(tenantId));
        quota.setCurrentRoles(baseMapper.countRolesByTenantId(tenantId));
        quota.setCurrentStorage(0L); // TODO: 实现存储使用量统计
        
        // 计算使用率
        quota.calculateUsageRates();
        
        return quota;
    }

    @Override
    public Boolean checkQuotaLimit(Long tenantId, String quotaType, Integer increment) {
        TenantQuotaDTO quota = getTenantQuota(tenantId);
        
        switch (quotaType.toLowerCase()) {
            case "users":
                return quota.getCurrentUsers() + increment <= quota.getMaxUsers();
            case "departments":
                return quota.getCurrentDepartments() + increment <= quota.getMaxDepartments();
            case "roles":
                return quota.getCurrentRoles() + increment <= quota.getMaxRoles();
            default:
                return true;
        }
    }

    @Override
    public Integer handleExpiringTenants(Integer days) {
        List<Tenant> expiringTenants = baseMapper.selectExpiringTenants(days);
        
        for (Tenant tenant : expiringTenants) {
            // 发送过期提醒通知
            sendExpirationNotification(tenant, days);
        }
        
        return expiringTenants.size();
    }

    @Override
    public Integer handleExpiredTenants() {
        List<Tenant> expiredTenants = baseMapper.selectExpiredTenants();
        
        for (Tenant tenant : expiredTenants) {
            // 更新为过期状态
            updateTenantStatus(tenant.getId(), Tenant.TenantStatus.EXPIRED.name(), "租户已过期");
            
            // 发送过期通知
            sendExpiredNotification(tenant);
        }
        
        return expiredTenants.size();
    }

    @Override
    public Boolean batchUpdateStatus(List<Long> ids, String status) {
        Integer count = baseMapper.batchUpdateStatus(ids, status, "system");
        return count > 0;
    }

    @Override
    public TenantStatisticsDTO getTenantStatistics() {
        TenantStatisticsDTO statistics = new TenantStatisticsDTO();
        
        // 基础统计
        LambdaQueryWrapper<Tenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tenant::getDelFlag, 0);
        statistics.setTotalTenants((long) count(queryWrapper));
        
        // 按状态统计
        queryWrapper.clear();
        queryWrapper.eq(Tenant::getDelFlag, 0).eq(Tenant::getStatus, "ACTIVE");
        statistics.setActiveTenants((long) count(queryWrapper));
        
        queryWrapper.clear();
        queryWrapper.eq(Tenant::getDelFlag, 0).eq(Tenant::getStatus, "SUSPENDED");
        statistics.setSuspendedTenants((long) count(queryWrapper));
        
        queryWrapper.clear();
        queryWrapper.eq(Tenant::getDelFlag, 0).eq(Tenant::getStatus, "EXPIRED");
        statistics.setExpiredTenants((long) count(queryWrapper));
        
        // 按类型统计
        Map<String, Long> tenantsByType = new HashMap<>();
        for (Tenant.TenantType type : Tenant.TenantType.values()) {
            queryWrapper.clear();
            queryWrapper.eq(Tenant::getDelFlag, 0).eq(Tenant::getType, type.name());
            tenantsByType.put(type.name(), (long) count(queryWrapper));
        }
        statistics.setTenantsByType(tenantsByType);
        
        // TODO: 完善其他统计信息
        
        return statistics;
    }

    /**
     * 设置租户默认值
     */
    private void setTenantDefaults(Tenant tenant, TenantCreateDTO createDTO) {
        tenant.setStatus(Tenant.TenantStatus.ACTIVE.name());
        tenant.setLevel(1);
        tenant.setSortOrder(0);
        tenant.setCreateTime(LocalDateTime.now());
        tenant.setUpdateTime(LocalDateTime.now());
        tenant.setCreateBy("system");
        tenant.setUpdateBy("system");
        tenant.setDelFlag(0);
        
        // 设置默认配额（如果未指定套餐）
        if (createDTO.getPackageId() == null) {
            setDefaultQuota(tenant, createDTO.getType());
        }
        
        // 设置默认配置
        if (tenant.getFeatureConfig() == null) {
            tenant.setFeatureConfig(getDefaultFeatureConfig(createDTO.getType()));
        }
        
        if (tenant.getThemeConfig() == null) {
            tenant.setThemeConfig(getDefaultThemeConfig());
        }
        
        if (tenant.getSecurityConfig() == null) {
            tenant.setSecurityConfig(getDefaultSecurityConfig());
        }
    }

    /**
     * 计算租户层级信息
     */
    private void calculateTenantHierarchy(Tenant tenant, Long parentId) {
        if (parentId == null || parentId == 0) {
            tenant.setParentId(0L);
            tenant.setLevel(1);
        } else {
            Tenant parentTenant = getById(parentId);
            if (parentTenant == null) {
                throw new ServiceException("父租户不存在");
            }
            tenant.setParentId(parentId);
            tenant.setLevel(parentTenant.getLevel() + 1);
        }
    }

    /**
     * 初始化租户数据
     */
    private void initializeTenantData(Tenant tenant) {
        // TODO: 创建租户默认角色和权限
        // TODO: 创建租户根组织
        // TODO: 创建租户管理员用户
        log.info("初始化租户数据：{}", tenant.getId());
    }

    /**
     * 更新租户状态
     */
    private Boolean updateTenantStatus(Long id, String status, String reason) {
        LambdaUpdateWrapper<Tenant> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Tenant::getId, id)
                    .set(Tenant::getStatus, status)
                    .set(Tenant::getUpdateTime, LocalDateTime.now())
                    .set(Tenant::getRemark, reason);
        
        return update(updateWrapper);
    }

    /**
     * 设置默认配额
     */
    private void setDefaultQuota(Tenant tenant, String type) {
        switch (type) {
            case "ENTERPRISE":
                tenant.setMaxUsers(1000);
                tenant.setMaxStorage(107374182400L); // 100GB
                tenant.setMaxDepartments(200);
                tenant.setMaxRoles(100);
                break;
            case "STANDARD":
                tenant.setMaxUsers(200);
                tenant.setMaxStorage(21474836480L); // 20GB
                tenant.setMaxDepartments(50);
                tenant.setMaxRoles(30);
                break;
            case "PERSONAL":
                tenant.setMaxUsers(10);
                tenant.setMaxStorage(1073741824L); // 1GB
                tenant.setMaxDepartments(5);
                tenant.setMaxRoles(5);
                break;
            default:
                tenant.setMaxUsers(100);
                tenant.setMaxStorage(5368709120L); // 5GB
                tenant.setMaxDepartments(20);
                tenant.setMaxRoles(10);
        }
    }

    /**
     * 获取默认功能配置
     */
    private Map<String, Object> getDefaultFeatureConfig(String type) {
        Map<String, Object> config = new HashMap<>();
        config.put("modules", List.of("user", "org", "role"));
        
        Map<String, Boolean> features = new HashMap<>();
        features.put("sso", !"PERSONAL".equals(type));
        features.put("mfa", "ENTERPRISE".equals(type));
        features.put("api", !"PERSONAL".equals(type));
        features.put("export", true);
        features.put("audit", "ENTERPRISE".equals(type));
        
        config.put("features", features);
        return config;
    }

    /**
     * 获取默认主题配置
     */
    private Map<String, Object> getDefaultThemeConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("primaryColor", "#1890ff");
        config.put("logo", "");
        config.put("title", "XingChen管理系统");
        return config;
    }

    /**
     * 获取默认安全配置
     */
    private Map<String, Object> getDefaultSecurityConfig() {
        Map<String, Object> config = new HashMap<>();
        
        Map<String, Object> passwordPolicy = new HashMap<>();
        passwordPolicy.put("minLength", 8);
        passwordPolicy.put("requireSpecialChar", true);
        passwordPolicy.put("requireNumber", true);
        passwordPolicy.put("requireUpperCase", true);
        passwordPolicy.put("maxAge", 90);
        
        Map<String, Object> sessionConfig = new HashMap<>();
        sessionConfig.put("timeout", 30);
        sessionConfig.put("maxConcurrent", 1);
        
        config.put("passwordPolicy", passwordPolicy);
        config.put("sessionConfig", sessionConfig);
        config.put("ipWhitelist", List.of());
        
        return config;
    }

    /**
     * 发送过期提醒通知
     */
    private void sendExpirationNotification(Tenant tenant, Integer days) {
        // TODO: 实现通知发送逻辑
        log.warn("租户即将过期：{}，剩余{}天", tenant.getName(), days);
    }

    /**
     * 发送过期通知
     */
    private void sendExpiredNotification(Tenant tenant) {
        // TODO: 实现通知发送逻辑
        log.warn("租户已过期：{}", tenant.getName());
    }
}
