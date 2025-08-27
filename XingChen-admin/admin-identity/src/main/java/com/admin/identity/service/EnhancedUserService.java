package com.admin.identity.service;

import com.admin.identity.domain.dto.*;
import com.admin.identity.domain.entity.User;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 增强用户服务接口（支持多租户和组织管理）
 *
 * @author admin
 * @since 2024-01-15
 */
public interface EnhancedUserService extends UserService {

    /**
     * 创建用户（增强版）
     *
     * @param tenantId 租户ID
     * @param createDTO 用户创建DTO
     * @return 用户ID
     */
    Long createUser(Long tenantId, UserCreateDTO createDTO);

    /**
     * 更新用户（增强版）
     *
     * @param tenantId 租户ID
     * @param updateDTO 用户更新DTO
     * @return 是否成功
     */
    Boolean updateUser(Long tenantId, UserUpdateDTO updateDTO);

    /**
     * 删除用户
     *
     * @param tenantId 租户ID
     * @param userId 用户ID
     * @return 是否成功
     */
    Boolean deleteUser(Long tenantId, Long userId);

    /**
     * 批量删除用户
     *
     * @param tenantId 租户ID
     * @param userIds 用户ID列表
     * @return 是否成功
     */
    Boolean batchDeleteUsers(Long tenantId, List<Long> userIds);

    /**
     * 分页查询用户列表
     *
     * @param tenantId 租户ID
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    IPage<UserResponseDTO> getUserPage(Long tenantId, UserQueryDTO queryDTO);

    /**
     * 查询用户详情
     *
     * @param tenantId 租户ID
     * @param userId 用户ID
     * @return 用户详情
     */
    UserDetailDTO getUserDetail(Long tenantId, Long userId);

    /**
     * 重置用户密码
     *
     * @param tenantId 租户ID
     * @param userId 用户ID
     * @param newPassword 新密码（如果为空则使用默认密码）
     * @return 是否成功
     */
    Boolean resetPassword(Long tenantId, Long userId, String newPassword);

    /**
     * 修改密码
     *
     * @param tenantId 租户ID
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否成功
     */
    Boolean changePassword(Long tenantId, Long userId, String oldPassword, String newPassword);

    /**
     * 锁定用户
     *
     * @param tenantId 租户ID
     * @param userId 用户ID
     * @param reason 锁定原因
     * @return 是否成功
     */
    Boolean lockUser(Long tenantId, Long userId, String reason);

    /**
     * 解锁用户
     *
     * @param tenantId 租户ID
     * @param userId 用户ID
     * @return 是否成功
     */
    Boolean unlockUser(Long tenantId, Long userId);

    /**
     * 启用用户
     *
     * @param tenantId 租户ID
     * @param userId 用户ID
     * @return 是否成功
     */
    Boolean enableUser(Long tenantId, Long userId);

    /**
     * 禁用用户
     *
     * @param tenantId 租户ID
     * @param userId 用户ID
     * @return 是否成功
     */
    Boolean disableUser(Long tenantId, Long userId);

    /**
     * 分配用户到组织
     *
     * @param tenantId 租户ID
     * @param userId 用户ID
     * @param orgId 组织ID
     * @param position 职位
     * @param isPrimary 是否主组织
     * @return 是否成功
     */
    Boolean assignUserToOrganization(Long tenantId, Long userId, Long orgId, String position, Boolean isPrimary);

    /**
     * 从组织中移除用户
     *
     * @param tenantId 租户ID
     * @param userId 用户ID
     * @param orgId 组织ID
     * @return 是否成功
     */
    Boolean removeUserFromOrganization(Long tenantId, Long userId, Long orgId);

    /**
     * 分配角色给用户
     *
     * @param tenantId 租户ID
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @param orgId 组织范围ID（可选）
     * @return 是否成功
     */
    Boolean assignRolesToUser(Long tenantId, Long userId, List<Long> roleIds, Long orgId);

    /**
     * 移除用户角色
     *
     * @param tenantId 租户ID
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 是否成功
     */
    Boolean removeRolesFromUser(Long tenantId, Long userId, List<Long> roleIds);

    /**
     * 查询用户的组织列表
     *
     * @param tenantId 租户ID
     * @param userId 用户ID
     * @return 组织列表
     */
    List<OrganizationResponseDTO> getUserOrganizations(Long tenantId, Long userId);

    /**
     * 查询组织下的用户列表
     *
     * @param tenantId 租户ID
     * @param orgId 组织ID
     * @param includeSubOrgs 是否包含子组织
     * @return 用户列表
     */
    List<UserResponseDTO> getOrganizationUsers(Long tenantId, Long orgId, Boolean includeSubOrgs);

    /**
     * 检查用户名是否存在
     *
     * @param tenantId 租户ID
     * @param username 用户名
     * @return 是否存在
     */
    Boolean existsByUsername(Long tenantId, String username);

    /**
     * 检查用户名是否存在（排除指定用户）
     *
     * @param tenantId 租户ID
     * @param username 用户名
     * @param excludeUserId 排除的用户ID
     * @return 是否存在
     */
    Boolean existsByUsernameExcludeId(Long tenantId, String username, Long excludeUserId);

    /**
     * 检查邮箱是否存在
     *
     * @param tenantId 租户ID
     * @param email 邮箱
     * @return 是否存在
     */
    Boolean existsByEmail(Long tenantId, String email);

    /**
     * 检查邮箱是否存在（排除指定用户）
     *
     * @param tenantId 租户ID
     * @param email 邮箱
     * @param excludeUserId 排除的用户ID
     * @return 是否存在
     */
    Boolean existsByEmailExcludeId(Long tenantId, String email, Long excludeUserId);

    /**
     * 检查手机号是否存在
     *
     * @param tenantId 租户ID
     * @param phone 手机号
     * @return 是否存在
     */
    Boolean existsByPhone(Long tenantId, String phone);

    /**
     * 检查员工工号是否存在
     *
     * @param tenantId 租户ID
     * @param employeeNo 员工工号
     * @return 是否存在
     */
    Boolean existsByEmployeeNo(Long tenantId, String employeeNo);

    /**
     * 批量导入用户
     *
     * @param tenantId 租户ID
     * @param users 用户列表
     * @return 导入结果
     */
    UserImportResultDTO importUsers(Long tenantId, List<UserCreateDTO> users);

    /**
     * 导出用户数据
     *
     * @param tenantId 租户ID
     * @param queryDTO 查询条件
     * @param format 导出格式（excel/csv）
     * @return 导出文件路径
     */
    String exportUsers(Long tenantId, UserQueryDTO queryDTO, String format);

    /**
     * 获取用户统计信息
     *
     * @param tenantId 租户ID
     * @return 统计信息
     */
    UserStatisticsDTO getUserStatistics(Long tenantId);

    /**
     * 批量转移用户组织
     *
     * @param tenantId 租户ID
     * @param userIds 用户ID列表
     * @param fromOrgId 原组织ID
     * @param toOrgId 目标组织ID
     * @return 是否成功
     */
    Boolean batchTransferUsers(Long tenantId, List<Long> userIds, Long fromOrgId, Long toOrgId);

    /**
     * 同步用户信息到外部系统
     *
     * @param tenantId 租户ID
     * @param userId 用户ID
     * @param targetSystems 目标系统列表
     * @return 同步结果
     */
    UserSyncResultDTO syncUserToExternalSystems(Long tenantId, Long userId, List<String> targetSystems);
}
