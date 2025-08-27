package com.admin.identity.domain.dto;

import lombok.Data;

import java.util.Map;

/**
 * 组织统计DTO
 *
 * @author admin
 * @since 2024-01-15
 */
@Data
public class OrganizationStatisticsDTO {

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 组织总数
     */
    private Long totalOrganizations;

    /**
     * 活跃组织数
     */
    private Long activeOrganizations;

    /**
     * 禁用组织数
     */
    private Long disabledOrganizations;

    /**
     * 最大层级
     */
    private Integer maxLevel;

    /**
     * 平均层级
     */
    private Double averageLevel;

    /**
     * 按类型统计
     */
    private Map<String, Long> organizationsByType;

    /**
     * 按层级统计
     */
    private Map<Integer, Long> organizationsByLevel;

    /**
     * 总成员数
     */
    private Long totalMembers;

    /**
     * 平均每个组织成员数
     */
    private Double averageMembersPerOrganization;

    /**
     * 有负责人的组织数
     */
    private Long organizationsWithLeader;

    /**
     * 没有负责人的组织数
     */
    private Long organizationsWithoutLeader;

    /**
     * 空组织数（没有成员的组织）
     */
    private Long emptyOrganizations;

    /**
     * 叶子节点组织数（没有子组织的组织）
     */
    private Long leafOrganizations;

    /**
     * 根组织数
     */
    private Long rootOrganizations;

    /**
     * 单人组织数（只有一个成员的组织）
     */
    private Long singleMemberOrganizations;

    /**
     * 大型组织数（成员数超过50的组织）
     */
    private Long largeOrganizations;
}
