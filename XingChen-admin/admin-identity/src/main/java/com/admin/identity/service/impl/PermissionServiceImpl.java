package com.admin.identity.service.impl;

import com.admin.identity.domain.entity.Permission;
import com.admin.identity.mapper.PermissionMapper;
import com.admin.identity.mapper.RolePermissionMapper;
import com.admin.identity.service.PermissionService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限服务实现类
 *
 * @author admin
 * @since 2024-01-15
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    private final RolePermissionMapper rolePermissionMapper;

    @Override
    public IPage<Permission> pagePermissions(Page<Permission> page, String permissionName, String permissionType, String status) {
        return baseMapper.selectPermissionPage(page, permissionName, permissionType, status);
    }

    @Override
    public List<Permission> listByRoleId(Long roleId) {
        return baseMapper.selectPermissionsByRoleId(roleId);
    }

    @Override
    public List<Permission> listByUserId(Long userId) {
        return baseMapper.selectPermissionsByUserId(userId);
    }

    @Override
    public Permission getByPermissionKey(String permissionKey) {
        return baseMapper.selectByPermissionKey(permissionKey);
    }

    @Override
    public boolean existsByPermissionKey(String permissionKey, Long excludeId) {
        return baseMapper.existsByPermissionKey(permissionKey, excludeId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Permission createPermission(Permission permission) {
        // 检查权限Key是否已存在
        if (existsByPermissionKey(permission.getPermissionKey(), null)) {
            throw new RuntimeException("权限标识已存在");
        }

        // 设置创建时间
        permission.setCreateTime(LocalDateTime.now());
        permission.setUpdateTime(LocalDateTime.now());
        permission.setDelFlag(0);

        // 如果没有指定排序，自动设置
        if (permission.getSortOrder() == null) {
            permission.setSortOrder(0);
        }

        // 保存权限
        boolean result = save(permission);
        if (!result) {
            throw new RuntimeException("创建权限失败");
        }

        log.info("创建权限成功：permissionId={}, permissionName={}", permission.getId(), permission.getPermissionName());
        return permission;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Permission updatePermission(Permission permission) {
        // 检查权限是否存在
        Permission existingPermission = getById(permission.getId());
        if (existingPermission == null) {
            throw new RuntimeException("权限不存在");
        }

        // 检查权限Key是否被其他权限使用
        if (existsByPermissionKey(permission.getPermissionKey(), permission.getId())) {
            throw new RuntimeException("权限标识已被其他权限使用");
        }

        // 设置更新时间
        permission.setUpdateTime(LocalDateTime.now());

        // 更新权限
        boolean result = updateById(permission);
        if (!result) {
            throw new RuntimeException("更新权限失败");
        }

        log.info("更新权限成功：permissionId={}, permissionName={}", permission.getId(), permission.getPermissionName());
        return permission;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePermission(Long permissionId) {
        // 检查权限是否存在
        Permission permission = getById(permissionId);
        if (permission == null) {
            throw new RuntimeException("权限不存在");
        }

        // 检查是否有子权限
        List<Permission> children = listByParentId(permissionId);
        if (!children.isEmpty()) {
            throw new RuntimeException("存在子权限，无法删除");
        }

        // 删除角色权限关联
        rolePermissionMapper.deleteByPermissionId(permissionId);

        // 逻辑删除权限
        permission.setDelFlag(1);
        permission.setUpdateTime(LocalDateTime.now());
        boolean result = updateById(permission);

        if (result) {
            log.info("删除权限成功：permissionId={}, permissionName={}", permissionId, permission.getPermissionName());
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(List<Long> permissionIds) {
        if (permissionIds == null || permissionIds.isEmpty()) {
            return true;
        }

        // 检查是否有子权限
        for (Long permissionId : permissionIds) {
            List<Permission> children = listByParentId(permissionId);
            if (!children.isEmpty()) {
                Permission permission = getById(permissionId);
                throw new RuntimeException("权限【" + permission.getPermissionName() + "】存在子权限，无法删除");
            }
        }

        // 删除角色权限关联
        for (Long permissionId : permissionIds) {
            rolePermissionMapper.deleteByPermissionId(permissionId);
        }

        // 批量逻辑删除权限
        int result = baseMapper.deleteByIds(permissionIds);

        if (result > 0) {
            log.info("批量删除权限成功：permissionIds={}, 删除数量={}", permissionIds, result);
        }

        return result > 0;
    }

    @Override
    public List<Permission> listByParentId(Long parentId) {
        return baseMapper.selectByParentId(parentId);
    }

    @Override
    public List<Permission> getMenuTree() {
        List<Permission> menuPermissions = baseMapper.selectMenuPermissions();
        return buildPermissionTree(menuPermissions);
    }

    @Override
    public List<Permission> buildPermissionTree(List<Permission> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return new ArrayList<>();
        }

        // 构建权限映射
        Map<Long, Permission> permissionMap = permissions.stream()
                .collect(Collectors.toMap(Permission::getId, p -> p));

        // 构建树结构
        List<Permission> tree = new ArrayList<>();
        for (Permission permission : permissions) {
            if (permission.getParentId() == null || permission.getParentId() == 0) {
                // 根节点
                tree.add(permission);
            } else {
                // 子节点
                Permission parent = permissionMap.get(permission.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(permission);
                }
            }
        }

        // 排序
        sortPermissionTree(tree);
        return tree;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changeStatus(Long permissionId, String status) {
        Permission permission = getById(permissionId);
        if (permission == null) {
            throw new RuntimeException("权限不存在");
        }

        permission.setStatus(status);
        permission.setUpdateTime(LocalDateTime.now());

        boolean result = updateById(permission);
        if (result) {
            log.info("权限状态变更成功：permissionId={}, status={}", permissionId, status);
        }

        return result;
    }

    @Override
    public List<String> getPermissionKeysByUserId(Long userId) {
        List<Permission> permissions = listByUserId(userId);
        return permissions.stream()
                .map(Permission::getPermissionKey)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 递归排序权限树
     */
    private void sortPermissionTree(List<Permission> permissions) {
        permissions.sort(Comparator.comparing(Permission::getSortOrder, Comparator.nullsLast(Comparator.naturalOrder())));
        for (Permission permission : permissions) {
            if (permission.getChildren() != null && !permission.getChildren().isEmpty()) {
                sortPermissionTree(permission.getChildren());
            }
        }
    }
}
