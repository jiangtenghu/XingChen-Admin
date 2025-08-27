package com.admin.identity.service;

import com.admin.common.core.exception.ServiceException;
import com.admin.identity.domain.dto.TenantCreateDTO;
import com.admin.identity.domain.dto.TenantQueryDTO;
import com.admin.identity.domain.dto.TenantResponseDTO;
import com.admin.identity.domain.entity.Tenant;
import com.admin.identity.mapper.TenantMapper;
import com.admin.identity.service.impl.TenantServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 租户服务测试
 *
 * @author admin
 * @since 2024-01-15
 */
@ExtendWith(MockitoExtension.class)
class TenantServiceTest {

    @Mock
    private TenantMapper tenantMapper;

    @InjectMocks
    private TenantServiceImpl tenantService;

    private TenantCreateDTO createDTO;
    private Tenant tenant;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        createDTO = new TenantCreateDTO();
        createDTO.setName("测试租户");
        createDTO.setCode("test_tenant");
        createDTO.setType("STANDARD");
        createDTO.setContactPerson("张三");
        createDTO.setContactPhone("13800138000");
        createDTO.setContactEmail("zhangsan@test.com");
        createDTO.setIndustry("软件");
        createDTO.setScale("MEDIUM");
        createDTO.setRegion("北京");
        
        tenant = new Tenant();
        tenant.setId(1L);
        tenant.setName(createDTO.getName());
        tenant.setCode(createDTO.getCode());
        tenant.setType(createDTO.getType());
        tenant.setStatus("ACTIVE");
        tenant.setParentId(0L);
        tenant.setLevel(1);
        tenant.setContactPerson(createDTO.getContactPerson());
        tenant.setContactPhone(createDTO.getContactPhone());
        tenant.setContactEmail(createDTO.getContactEmail());
        tenant.setIndustry(createDTO.getIndustry());
        tenant.setScale(createDTO.getScale());
        tenant.setRegion(createDTO.getRegion());
        tenant.setMaxUsers(200);
        tenant.setMaxStorage(21474836480L);
        tenant.setMaxDepartments(50);
        tenant.setMaxRoles(30);
        tenant.setCreateTime(LocalDateTime.now());
        tenant.setUpdateTime(LocalDateTime.now());
        tenant.setCreateBy("system");
        tenant.setUpdateBy("system");
        tenant.setDelFlag(0);
    }

    @Test
    void testCreateTenant_Success() {
        // 模拟数据库查询结果
        when(tenantMapper.countByCode(createDTO.getCode())).thenReturn(0);
        when(tenantMapper.insert(any(Tenant.class))).thenReturn(1);
        
        // 执行测试
        Long tenantId = tenantService.createTenant(createDTO);
        
        // 验证结果
        assertNotNull(tenantId);
        verify(tenantMapper, times(1)).countByCode(createDTO.getCode());
        verify(tenantMapper, times(1)).insert(any(Tenant.class));
    }

    @Test
    void testCreateTenant_DuplicateCode() {
        // 模拟编码已存在
        when(tenantMapper.countByCode(createDTO.getCode())).thenReturn(1);
        
        // 执行测试并验证异常
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            tenantService.createTenant(createDTO);
        });
        
        assertEquals("租户编码已存在：" + createDTO.getCode(), exception.getMessage());
        verify(tenantMapper, times(1)).countByCode(createDTO.getCode());
        verify(tenantMapper, never()).insert(any(Tenant.class));
    }

    @Test
    void testCreateTenant_DuplicateDomain() {
        // 设置域名
        createDTO.setDomain("test.example.com");
        
        // 模拟编码不存在但域名已存在
        when(tenantMapper.countByCode(createDTO.getCode())).thenReturn(0);
        when(tenantMapper.countByDomain(createDTO.getDomain())).thenReturn(1);
        
        // 执行测试并验证异常
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            tenantService.createTenant(createDTO);
        });
        
        assertEquals("域名已被使用：" + createDTO.getDomain(), exception.getMessage());
        verify(tenantMapper, times(1)).countByCode(createDTO.getCode());
        verify(tenantMapper, times(1)).countByDomain(createDTO.getDomain());
        verify(tenantMapper, never()).insert(any(Tenant.class));
    }

    @Test
    void testGetTenantPage_Success() {
        // 准备查询条件
        TenantQueryDTO queryDTO = new TenantQueryDTO();
        queryDTO.setName("测试");
        queryDTO.setType("STANDARD");
        queryDTO.setPageNum(1);
        queryDTO.setPageSize(10);
        
        // 准备返回数据
        Page<TenantResponseDTO> page = new Page<>(1, 10);
        TenantResponseDTO responseDTO = new TenantResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setName("测试租户");
        responseDTO.setCode("test_tenant");
        responseDTO.setType("STANDARD");
        responseDTO.setStatus("ACTIVE");
        page.getRecords().add(responseDTO);
        page.setTotal(1);
        
        // 模拟数据库查询
        when(tenantMapper.selectTenantPage(any(Page.class), any(TenantQueryDTO.class))).thenReturn(page);
        
        // 执行测试
        IPage<TenantResponseDTO> result = tenantService.getTenantPage(queryDTO);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRecords().size());
        assertEquals("测试租户", result.getRecords().get(0).getName());
        verify(tenantMapper, times(1)).selectTenantPage(any(Page.class), any(TenantQueryDTO.class));
    }

    @Test
    void testExistsByCode_True() {
        // 模拟编码存在
        when(tenantMapper.countByCode("test_code")).thenReturn(1);
        
        // 执行测试
        Boolean exists = tenantService.existsByCode("test_code");
        
        // 验证结果
        assertTrue(exists);
        verify(tenantMapper, times(1)).countByCode("test_code");
    }

    @Test
    void testExistsByCode_False() {
        // 模拟编码不存在
        when(tenantMapper.countByCode("test_code")).thenReturn(0);
        
        // 执行测试
        Boolean exists = tenantService.existsByCode("test_code");
        
        // 验证结果
        assertFalse(exists);
        verify(tenantMapper, times(1)).countByCode("test_code");
    }

    @Test
    void testActivateTenant_Success() {
        // 模拟更新成功
        when(tenantMapper.updateById(any(Tenant.class))).thenReturn(1);
        
        // 执行测试
        Boolean result = tenantService.activateTenant(1L);
        
        // 验证结果
        assertTrue(result);
        verify(tenantMapper, times(1)).updateById(any(Tenant.class));
    }

    @Test
    void testRenewTenant_Success() {
        // 准备租户数据
        tenant.setExpireTime(LocalDateTime.now().plusDays(30));
        
        // 模拟数据库操作
        when(tenantMapper.selectById(1L)).thenReturn(tenant);
        when(tenantMapper.updateById(any(Tenant.class))).thenReturn(1);
        
        // 执行测试
        Boolean result = tenantService.renewTenant(1L, 12);
        
        // 验证结果
        assertTrue(result);
        verify(tenantMapper, times(1)).selectById(1L);
        verify(tenantMapper, times(1)).updateById(any(Tenant.class));
    }

    @Test
    void testRenewTenant_TenantNotFound() {
        // 模拟租户不存在
        when(tenantMapper.selectById(1L)).thenReturn(null);
        
        // 执行测试并验证异常
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            tenantService.renewTenant(1L, 12);
        });
        
        assertEquals("租户不存在", exception.getMessage());
        verify(tenantMapper, times(1)).selectById(1L);
        verify(tenantMapper, never()).updateById(any(Tenant.class));
    }

    @Test
    void testGetTenantQuota_Success() {
        // 模拟数据库查询
        when(tenantMapper.selectById(1L)).thenReturn(tenant);
        when(tenantMapper.countUsersByTenantId(1L)).thenReturn(50);
        when(tenantMapper.countDepartmentsByTenantId(1L)).thenReturn(10);
        when(tenantMapper.countRolesByTenantId(1L)).thenReturn(5);
        
        // 执行测试
        var quota = tenantService.getTenantQuota(1L);
        
        // 验证结果
        assertNotNull(quota);
        assertEquals(1L, quota.getTenantId());
        assertEquals("测试租户", quota.getTenantName());
        assertEquals(200, quota.getMaxUsers());
        assertEquals(50, quota.getCurrentUsers());
        assertEquals(25.0, quota.getUserUsageRate());
        assertFalse(quota.getUserQuotaWarning());
        
        verify(tenantMapper, times(1)).selectById(1L);
        verify(tenantMapper, times(1)).countUsersByTenantId(1L);
        verify(tenantMapper, times(1)).countDepartmentsByTenantId(1L);
        verify(tenantMapper, times(1)).countRolesByTenantId(1L);
    }

    @Test
    void testCheckQuotaLimit_WithinLimit() {
        // 准备测试数据
        when(tenantMapper.selectById(1L)).thenReturn(tenant);
        when(tenantMapper.countUsersByTenantId(1L)).thenReturn(50);
        when(tenantMapper.countDepartmentsByTenantId(1L)).thenReturn(10);
        when(tenantMapper.countRolesByTenantId(1L)).thenReturn(5);
        
        // 执行测试
        Boolean withinLimit = tenantService.checkQuotaLimit(1L, "users", 10);
        
        // 验证结果
        assertTrue(withinLimit);
    }

    @Test
    void testCheckQuotaLimit_ExceedLimit() {
        // 准备测试数据
        when(tenantMapper.selectById(1L)).thenReturn(tenant);
        when(tenantMapper.countUsersByTenantId(1L)).thenReturn(195);
        when(tenantMapper.countDepartmentsByTenantId(1L)).thenReturn(10);
        when(tenantMapper.countRolesByTenantId(1L)).thenReturn(5);
        
        // 执行测试
        Boolean withinLimit = tenantService.checkQuotaLimit(1L, "users", 10);
        
        // 验证结果
        assertFalse(withinLimit);
    }
}
