package com.admin.identity.domain.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 组织查询DTO
 *
 * @author admin
 * @since 2024-01-15
 */
@Data
public class OrganizationQueryDTO {

    /**
     * 组织名称（模糊查询）
     */
    private String orgName;

    /**
     * 组织编码（模糊查询）
     */
    private String orgCode;

    /**
     * 组织类型
     */
    private String orgType;

    /**
     * 父组织ID
     */
    private Long parentId;

    /**
     * 组织层级
     */
    private Integer level;

    /**
     * 负责人ID
     */
    private Long leaderId;

    /**
     * 负责人姓名（模糊查询）
     */
    private String leaderName;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 组织状态
     */
    private String status;

    /**
     * 创建时间开始
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeStart;

    /**
     * 创建时间结束
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeEnd;

    /**
     * 是否包含子组织
     */
    private Boolean includeChildren = false;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 每页数量
     */
    private Integer pageSize = 10;

    /**
     * 排序字段
     */
    private String orderBy = "sort_order";

    /**
     * 排序方向（asc/desc）
     */
    private String sortOrder = "asc";
}
