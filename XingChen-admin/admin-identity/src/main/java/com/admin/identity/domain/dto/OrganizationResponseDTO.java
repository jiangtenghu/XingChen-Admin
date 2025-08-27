package com.admin.identity.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 组织响应DTO
 *
 * @author admin
 * @since 2024-01-15
 */
@Data
public class OrganizationResponseDTO {

    /**
     * 组织ID
     */
    private Long id;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 组织名称
     */
    private String orgName;

    /**
     * 组织编码
     */
    private String orgCode;

    /**
     * 组织类型
     */
    private String orgType;

    /**
     * 组织类型描述
     */
    private String orgTypeDesc;

    /**
     * 父组织ID
     */
    private Long parentId;

    /**
     * 父组织名称
     */
    private String parentName;

    /**
     * 祖级列表
     */
    private String ancestors;

    /**
     * 组织层级
     */
    private Integer level;

    /**
     * 显示顺序
     */
    private Integer sortOrder;

    /**
     * 负责人ID
     */
    private Long leaderId;

    /**
     * 负责人姓名
     */
    private String leaderName;

    /**
     * 副负责人ID
     */
    private Long deputyLeaderId;

    /**
     * 副负责人姓名
     */
    private String deputyLeaderName;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 地址
     */
    private String address;

    /**
     * 成员数量
     */
    private Integer memberCount;

    /**
     * 直接子组织数量
     */
    private Integer childrenCount;

    /**
     * 组织状态
     */
    private String status;

    /**
     * 状态描述
     */
    private String statusDesc;

    /**
     * 组织配置
     */
    private Map<String, Object> orgConfig;

    /**
     * 子组织列表（树形结构使用）
     */
    private List<OrganizationResponseDTO> children;

    /**
     * 是否有子节点
     */
    private Boolean hasChildren;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 计算是否有子节点
     */
    public void calculateHasChildren() {
        this.hasChildren = this.children != null && !this.children.isEmpty();
    }
}
