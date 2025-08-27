package com.admin.identity.service.impl;

import cn.hutool.core.util.StrUtil;
import com.admin.common.core.exception.ServiceException;
import com.admin.identity.domain.dto.*;
import com.admin.identity.domain.entity.Organization;
import com.admin.identity.mapper.OrganizationMapper;
import com.admin.identity.service.OrganizationService;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * 组织架构服务实现
 *
 * @author admin
 * @since 2024-01-15
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements OrganizationService {

    @Override
    public Long createOrganization(Long tenantId, OrganizationCreateDTO createDTO) {
        log.info("开始创建组织：租户ID={}，组织名称={}", tenantId, createDTO.getOrgName());
        
        // 1. 验证组织编码唯一性
        if (existsByOrgCode(tenantId, createDTO.getOrgCode())) {
            throw new ServiceException("组织编码已存在：" + createDTO.getOrgCode());
        }
        
        // 2. 验证父组织存在性
        Organization parentOrg = null;
        if (createDTO.getParentId() != null && createDTO.getParentId() > 0) {
            parentOrg = getById(createDTO.getParentId());
            if (parentOrg == null || !parentOrg.getTenantId().equals(tenantId)) {
                throw new ServiceException("父组织不存在");
            }
        }
        
        // 3. 创建组织实体
        Organization organization = new Organization();
        BeanUtils.copyProperties(createDTO, organization);
        
        // 4. 设置默认值和层级信息
        setOrganizationDefaults(organization, tenantId, parentOrg);
        
        // 5. 计算祖级信息
        calculateAncestors(organization, parentOrg);
        
        // 6. 设置排序号
        if (organization.getSortOrder() == null) {
            Integer maxSortOrder = baseMapper.selectMaxSortOrderByParent(tenantId, organization.getParentId());
            organization.setSortOrder(maxSortOrder + 10);
        }
        
        // 7. 保存组织
        boolean result = save(organization);
        if (!result) {
            throw new ServiceException("创建组织失败");
        }
        
        log.info("组织创建成功，ID：{}，编码：{}", organization.getId(), organization.getOrgCode());
        return organization.getId();
    }

    @Override
    public Boolean updateOrganization(Long tenantId, OrganizationUpdateDTO updateDTO) {
        log.info("开始更新组织：租户ID={}，组织ID={}", tenantId, updateDTO.getId());
        
        // 1. 检查组织是否存在
        Organization existingOrg = getById(updateDTO.getId());
        if (existingOrg == null || !existingOrg.getTenantId().equals(tenantId)) {
            throw new ServiceException("组织不存在");
        }
        
        // 2. 如果更新父组织，需要特殊处理
        if (updateDTO.getParentId() != null && !updateDTO.getParentId().equals(existingOrg.getParentId())) {
            return moveOrganization(tenantId, updateDTO.getId(), updateDTO.getParentId(), updateDTO.getSortOrder());
        }
        
        // 3. 更新组织信息
        Organization organization = new Organization();
        BeanUtils.copyProperties(updateDTO, organization);
        organization.setUpdateTime(LocalDateTime.now());
        
        boolean result = updateById(organization);
        if (!result) {
            throw new ServiceException("更新组织失败");
        }
        
        log.info("组织更新成功：{}", updateDTO.getId());
        return true;
    }

    @Override
    public Boolean deleteOrganization(Long tenantId, Long id) {
        log.info("开始删除组织：租户ID={}，组织ID={}", tenantId, id);
        
        // 1. 检查组织是否存在
        Organization organization = getById(id);
        if (organization == null || !organization.getTenantId().equals(tenantId)) {
            throw new ServiceException("组织不存在");
        }
        
        // 2. 检查是否有子组织
        if (hasChildOrganizations(tenantId, id)) {
            throw new ServiceException("存在子组织，无法删除");
        }
        
        // 3. 检查是否有成员
        if (hasMembers(tenantId, id, false)) {
            throw new ServiceException("组织下存在成员，无法删除");
        }
        
        // 4. 执行删除
        boolean result = removeById(id);
        if (!result) {
            throw new ServiceException("删除组织失败");
        }
        
        log.info("组织删除成功：{}", id);
        return true;
    }

    @Override
    public Boolean batchDeleteOrganizations(Long tenantId, List<Long> ids) {
        log.info("开始批量删除组织：租户ID={}，组织IDs={}", tenantId, ids);
        
        for (Long id : ids) {
            deleteOrganization(tenantId, id);
        }
        
        log.info("批量删除组织成功：{}", ids);
        return true;
    }

    @Override
    public IPage<OrganizationResponseDTO> getOrganizationPage(Long tenantId, OrganizationQueryDTO queryDTO) {
        Page<OrganizationResponseDTO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        // 在查询条件中添加租户ID过滤
        return baseMapper.selectOrganizationPage(page, queryDTO);
    }

    @Override
    public List<OrganizationResponseDTO> getOrganizationTree(Long tenantId, OrganizationQueryDTO queryDTO) {
        // 1. 查询所有组织
        List<OrganizationResponseDTO> allOrganizations = baseMapper.selectOrganizationTree(tenantId, queryDTO);
        
        // 2. 构建树形结构
        return buildOrganizationTree(allOrganizations);
    }

    @Override
    public OrganizationResponseDTO getOrganizationDetail(Long tenantId, Long id) {
        OrganizationResponseDTO detail = baseMapper.selectOrganizationDetailById(tenantId, id);
        if (detail == null) {
            throw new ServiceException("组织不存在");
        }
        
        // 设置描述信息
        setOrganizationDescriptions(detail);
        
        return detail;
    }

    @Override
    public List<OrganizationResponseDTO> getChildOrganizations(Long tenantId, Long parentId) {
        return baseMapper.selectChildOrganizations(tenantId, parentId);
    }

    @Override
    public Boolean moveOrganization(Long tenantId, Long orgId, Long newParentId, Integer newSortOrder) {
        log.info("开始移动组织：租户ID={}，组织ID={}，新父组织ID={}", tenantId, orgId, newParentId);
        
        // 1. 获取当前组织
        Organization organization = getById(orgId);
        if (organization == null || !organization.getTenantId().equals(tenantId)) {
            throw new ServiceException("组织不存在");
        }
        
        // 2. 验证新父组织
        Organization newParent = null;
        if (newParentId != null && newParentId > 0) {
            newParent = getById(newParentId);
            if (newParent == null || !newParent.getTenantId().equals(tenantId)) {
                throw new ServiceException("新父组织不存在");
            }
            
            // 检查是否形成循环引用
            if (isCircularReference(tenantId, orgId, newParentId)) {
                throw new ServiceException("不能移动到自己的子组织下");
            }
        }
        
        // 3. 计算新的层级和祖级信息
        String oldAncestors = organization.getAncestors();
        calculateAncestors(organization, newParent);
        organization.setParentId(newParentId == null ? 0L : newParentId);
        
        // 4. 设置排序号
        if (newSortOrder != null) {
            organization.setSortOrder(newSortOrder);
        } else {
            Integer maxSortOrder = baseMapper.selectMaxSortOrderByParent(tenantId, newParentId);
            organization.setSortOrder(maxSortOrder + 10);
        }
        
        organization.setUpdateTime(LocalDateTime.now());
        
        // 5. 更新组织信息
        boolean result = updateById(organization);
        if (!result) {
            throw new ServiceException("移动组织失败");
        }
        
        // 6. 更新所有子组织的祖级信息
        String newAncestors = organization.getAncestors();
        baseMapper.batchUpdateChildrenAncestors(tenantId, oldAncestors + "," + orgId, newAncestors + "," + orgId);
        
        log.info("组织移动成功：{}", orgId);
        return true;
    }

    @Override
    public Boolean updateOrganizationSort(Long tenantId, Long orgId, Integer sortOrder) {
        LambdaUpdateWrapper<Organization> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Organization::getId, orgId)
                    .eq(Organization::getTenantId, tenantId)
                    .set(Organization::getSortOrder, sortOrder)
                    .set(Organization::getUpdateTime, LocalDateTime.now());
        
        return update(updateWrapper);
    }

    @Override
    public Boolean setOrganizationLeader(Long tenantId, Long orgId, Long leaderId, Long deputyLeaderId) {
        LambdaUpdateWrapper<Organization> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Organization::getId, orgId)
                    .eq(Organization::getTenantId, tenantId)
                    .set(Organization::getLeaderId, leaderId)
                    .set(Organization::getDeputyLeaderId, deputyLeaderId)
                    .set(Organization::getUpdateTime, LocalDateTime.now());
        
        return update(updateWrapper);
    }

    @Override
    public Boolean existsByOrgCode(Long tenantId, String orgCode) {
        return baseMapper.countByTenantIdAndOrgCode(tenantId, orgCode) > 0;
    }

    @Override
    public Boolean existsByOrgCodeExcludeId(Long tenantId, String orgCode, Long excludeId) {
        return baseMapper.countByTenantIdAndOrgCodeExcludeId(tenantId, orgCode, excludeId) > 0;
    }

    @Override
    public Boolean hasChildOrganizations(Long tenantId, Long parentId) {
        return baseMapper.countByTenantIdAndParentId(tenantId, parentId) > 0;
    }

    @Override
    public Boolean hasMembers(Long tenantId, Long orgId, Boolean includeSubOrgs) {
        Integer memberCount = baseMapper.countMembersByOrgId(orgId);
        if (memberCount > 0) {
            return true;
        }
        
        if (includeSubOrgs) {
            List<Long> subOrgIds = getAllSubOrganizationIds(tenantId, orgId);
            for (Long subOrgId : subOrgIds) {
                if (baseMapper.countMembersByOrgId(subOrgId) > 0) {
                    return true;
                }
            }
        }
        
        return false;
    }

    @Override
    public List<Long> getAllSubOrganizationIds(Long tenantId, Long parentId) {
        return baseMapper.selectAllSubOrganizationIds(tenantId, parentId);
    }

    @Override
    public List<OrganizationResponseDTO> getOrganizationsByLeader(Long tenantId, Long leaderId) {
        return baseMapper.selectOrganizationsByLeaderId(tenantId, leaderId);
    }

    @Override
    public List<OrganizationResponseDTO> getUserOrganizations(Long tenantId, Long userId) {
        return baseMapper.selectUserOrganizations(tenantId, userId);
    }

    @Override
    public Boolean enableOrganization(Long tenantId, Long id) {
        return updateOrganizationStatus(tenantId, id, Organization.OrgStatus.NORMAL.getCode());
    }

    @Override
    public Boolean disableOrganization(Long tenantId, Long id) {
        return updateOrganizationStatus(tenantId, id, Organization.OrgStatus.DISABLED.getCode());
    }

    @Override
    public Boolean batchUpdateStatus(Long tenantId, List<Long> ids, String status) {
        Integer count = baseMapper.batchUpdateStatus(tenantId, ids, status, "system");
        return count > 0;
    }

    @Override
    public Long initRootOrganization(Long tenantId, String tenantName) {
        log.info("初始化租户根组织：租户ID={}，租户名称={}", tenantId, tenantName);
        
        // 检查是否已存在根组织
        LambdaQueryWrapper<Organization> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Organization::getTenantId, tenantId)
                   .eq(Organization::getParentId, 0)
                   .eq(Organization::getDelFlag, 0);
        Organization existingRoot = getOne(queryWrapper);
        
        if (existingRoot != null) {
            log.info("租户根组织已存在：{}", existingRoot.getId());
            return existingRoot.getId();
        }
        
        // 创建根组织
        Organization rootOrg = new Organization();
        rootOrg.setTenantId(tenantId);
        rootOrg.setOrgName(tenantName);
        rootOrg.setOrgCode("ROOT");
        rootOrg.setOrgType(Organization.OrgType.COMPANY.name());
        rootOrg.setParentId(0L);
        rootOrg.setAncestors("0");
        rootOrg.setLevel(1);
        rootOrg.setSortOrder(0);
        rootOrg.setStatus(Organization.OrgStatus.NORMAL.getCode());
        rootOrg.setCreateTime(LocalDateTime.now());
        rootOrg.setUpdateTime(LocalDateTime.now());
        rootOrg.setCreateBy("system");
        rootOrg.setUpdateBy("system");
        rootOrg.setDelFlag(0);
        
        boolean result = save(rootOrg);
        if (!result) {
            throw new ServiceException("初始化根组织失败");
        }
        
        log.info("租户根组织初始化成功：{}", rootOrg.getId());
        return rootOrg.getId();
    }

    @Override
    public OrganizationImportResultDTO importOrganizations(Long tenantId, List<OrganizationCreateDTO> organizations) {
        // TODO: 实现组织导入功能
        log.info("导入组织架构：租户ID={}，数量={}", tenantId, organizations.size());
        return new OrganizationImportResultDTO();
    }

    @Override
    public String exportOrganizations(Long tenantId, String format) {
        // TODO: 实现组织导出功能
        log.info("导出组织架构：租户ID={}，格式={}", tenantId, format);
        return "";
    }

    @Override
    public OrganizationStatisticsDTO getOrganizationStatistics(Long tenantId) {
        // TODO: 实现组织统计功能
        log.info("查询组织统计：租户ID={}", tenantId);
        return new OrganizationStatisticsDTO();
    }

    /**
     * 设置组织默认值
     */
    private void setOrganizationDefaults(Organization organization, Long tenantId, Organization parentOrg) {
        organization.setTenantId(tenantId);
        organization.setStatus(Organization.OrgStatus.NORMAL.getCode());
        organization.setCreateTime(LocalDateTime.now());
        organization.setUpdateTime(LocalDateTime.now());
        organization.setCreateBy("system");
        organization.setUpdateBy("system");
        organization.setDelFlag(0);
        
        // 设置层级
        if (parentOrg == null) {
            organization.setLevel(1);
            organization.setParentId(0L);
        } else {
            organization.setLevel(parentOrg.getLevel() + 1);
            organization.setParentId(parentOrg.getId());
        }
    }

    /**
     * 计算祖级信息
     */
    private void calculateAncestors(Organization organization, Organization parentOrg) {
        if (parentOrg == null) {
            organization.setAncestors("0");
        } else {
            organization.setAncestors(parentOrg.getAncestors() + "," + parentOrg.getId());
        }
    }

    /**
     * 检查是否形成循环引用
     */
    private boolean isCircularReference(Long tenantId, Long orgId, Long newParentId) {
        if (newParentId == null || newParentId.equals(orgId)) {
            return true;
        }
        
        List<Long> subOrgIds = getAllSubOrganizationIds(tenantId, orgId);
        return subOrgIds.contains(newParentId);
    }

    /**
     * 构建组织树
     */
    private List<OrganizationResponseDTO> buildOrganizationTree(List<OrganizationResponseDTO> allOrganizations) {
        if (allOrganizations == null || allOrganizations.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 按ID分组
        Map<Long, OrganizationResponseDTO> orgMap = allOrganizations.stream()
            .collect(Collectors.toMap(OrganizationResponseDTO::getId, org -> org));
        
        List<OrganizationResponseDTO> rootOrganizations = new ArrayList<>();
        
        for (OrganizationResponseDTO org : allOrganizations) {
            if (org.getParentId() == null || org.getParentId() == 0) {
                rootOrganizations.add(org);
            } else {
                OrganizationResponseDTO parent = orgMap.get(org.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(org);
                }
            }
        }
        
        // 设置hasChildren标志
        setHasChildrenFlag(allOrganizations);
        
        return rootOrganizations;
    }

    /**
     * 设置hasChildren标志
     */
    private void setHasChildrenFlag(List<OrganizationResponseDTO> organizations) {
        for (OrganizationResponseDTO org : organizations) {
            org.calculateHasChildren();
            if (org.getChildren() != null && !org.getChildren().isEmpty()) {
                setHasChildrenFlag(org.getChildren());
            }
        }
    }

    /**
     * 设置组织描述信息
     */
    private void setOrganizationDescriptions(OrganizationResponseDTO organization) {
        // 设置类型描述
        if (StrUtil.isNotBlank(organization.getOrgType())) {
            Organization.OrgType orgType = Organization.OrgType.valueOf(organization.getOrgType());
            organization.setOrgTypeDesc(orgType.getDescription());
        }
        
        // 设置状态描述
        if (StrUtil.isNotBlank(organization.getStatus())) {
            Organization.OrgStatus orgStatus = Organization.OrgStatus.valueOf(organization.getStatus());
            organization.setStatusDesc(orgStatus.getDescription());
        }
    }

    /**
     * 更新组织状态
     */
    private Boolean updateOrganizationStatus(Long tenantId, Long id, String status) {
        LambdaUpdateWrapper<Organization> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Organization::getId, id)
                    .eq(Organization::getTenantId, tenantId)
                    .set(Organization::getStatus, status)
                    .set(Organization::getUpdateTime, LocalDateTime.now());
        
        return update(updateWrapper);
    }
}
