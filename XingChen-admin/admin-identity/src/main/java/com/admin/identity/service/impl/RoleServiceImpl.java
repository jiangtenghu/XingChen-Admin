package com.admin.identity.service.impl;

import com.admin.identity.domain.entity.Role;
import com.admin.identity.domain.entity.RolePermission;
import com.admin.identity.mapper.RoleMapper;
import com.admin.identity.mapper.RolePermissionMapper;
import com.admin.identity.mapper.UserRoleMapper;
import com.admin.identity.service.RoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色服务实现类
 *
 * @author admin
 * @since 2024-01-15
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    private final RolePermissionMapper rolePermissionMapper;
    private final UserRoleMapper userRoleMapper;

    @Override
    public IPage<Role> pageByTenant(Page<Role> page, Long tenantId, String roleName, String status) {
        return baseMapper.selectRolePageByTenant(page, tenantId, roleName, status);
    }

    @Override
    public List<Role> listByUserId(Long userId) {
        return baseMapper.selectRolesByUserId(userId);
    }

    @Override
    public Role getByRoleKey(String roleKey, Long tenantId) {
        return baseMapper.selectByRoleKey(roleKey, tenantId);
    }

    @Override
    public boolean existsByRoleKey(String roleKey, Long tenantId, Long excludeId) {
        return baseMapper.existsByRoleKey(roleKey, tenantId, excludeId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Role createRole(Role role) {
        // 检查角色Key是否已存在
        if (existsByRoleKey(role.getRoleKey(), role.getTenantId(), null)) {
            throw new RuntimeException("角色标识已存在");
        }

        // 设置创建时间
        role.setCreateTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());
        role.setDelFlag(0);

        // 保存角色
        boolean result = save(role);
        if (!result) {
            throw new RuntimeException("创建角色失败");
        }

        log.info("创建角色成功：roleId={}, roleName={}", role.getId(), role.getRoleName());
        return role;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Role updateRole(Role role) {
        // 检查角色是否存在
        Role existingRole = getById(role.getId());
        if (existingRole == null) {
            throw new RuntimeException("角色不存在");
        }

        // 检查角色Key是否被其他角色使用
        if (existsByRoleKey(role.getRoleKey(), role.getTenantId(), role.getId())) {
            throw new RuntimeException("角色标识已被其他角色使用");
        }

        // 设置更新时间
        role.setUpdateTime(LocalDateTime.now());

        // 更新角色
        boolean result = updateById(role);
        if (!result) {
            throw new RuntimeException("更新角色失败");
        }

        log.info("更新角色成功：roleId={}, roleName={}", role.getId(), role.getRoleName());
        return role;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRole(Long roleId) {
        // 检查角色是否存在
        Role role = getById(roleId);
        if (role == null) {
            throw new RuntimeException("角色不存在");
        }

        // 删除角色权限关联
        rolePermissionMapper.deleteByRoleId(roleId);

        // 删除用户角色关联
        userRoleMapper.deleteByRoleId(roleId);

        // 逻辑删除角色
        role.setDelFlag(1);
        role.setUpdateTime(LocalDateTime.now());
        boolean result = updateById(role);

        if (result) {
            log.info("删除角色成功：roleId={}, roleName={}", roleId, role.getRoleName());
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return true;
        }

        // 删除角色权限关联
        rolePermissionMapper.deleteByRoleIds(roleIds);

        // 删除用户角色关联
        for (Long roleId : roleIds) {
            userRoleMapper.deleteByRoleId(roleId);
        }

        // 批量逻辑删除角色
        int result = baseMapper.deleteByIds(roleIds);

        if (result > 0) {
            log.info("批量删除角色成功：roleIds={}, 删除数量={}", roleIds, result);
        }

        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignPermissions(Long roleId, List<Long> permissionIds) {
        // 检查角色是否存在
        Role role = getById(roleId);
        if (role == null) {
            throw new RuntimeException("角色不存在");
        }

        // 删除原有权限关联
        rolePermissionMapper.deleteByRoleId(roleId);

        // 添加新的权限关联
        if (permissionIds != null && !permissionIds.isEmpty()) {
            List<RolePermission> rolePermissions = permissionIds.stream()
                    .map(permissionId -> {
                        RolePermission rp = new RolePermission();
                        rp.setRoleId(roleId);
                        rp.setPermissionId(permissionId);
                        rp.setCreateTime(LocalDateTime.now());
                        return rp;
                    })
                    .collect(Collectors.toList());

            int insertCount = rolePermissionMapper.batchInsert(rolePermissions);
            if (insertCount != rolePermissions.size()) {
                throw new RuntimeException("分配权限失败");
            }
        }

        log.info("角色权限分配成功：roleId={}, permissionIds={}", roleId, permissionIds);
        return true;
    }

    @Override
    public int countByTenantId(Long tenantId) {
        return baseMapper.countByTenantId(tenantId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changeStatus(Long roleId, String status) {
        Role role = getById(roleId);
        if (role == null) {
            throw new RuntimeException("角色不存在");
        }

        role.setStatus(status);
        role.setUpdateTime(LocalDateTime.now());

        boolean result = updateById(role);
        if (result) {
            log.info("角色状态变更成功：roleId={}, status={}", roleId, status);
        }

        return result;
    }
}
