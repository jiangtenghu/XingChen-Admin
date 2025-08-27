package com.admin.identity.domain.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 租户查询DTO
 *
 * @author admin
 * @since 2024-01-15
 */
@Data
public class TenantQueryDTO {

    /**
     * 租户名称（模糊查询）
     */
    private String name;

    /**
     * 租户编码（模糊查询）
     */
    private String code;

    /**
     * 租户类型
     */
    private String type;

    /**
     * 租户状态
     */
    private String status;

    /**
     * 父租户ID
     */
    private Long parentId;

    /**
     * 联系人（模糊查询）
     */
    private String contactPerson;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 联系邮箱
     */
    private String contactEmail;

    /**
     * 所属行业
     */
    private String industry;

    /**
     * 企业规模
     */
    private String scale;

    /**
     * 所在地区（模糊查询）
     */
    private String region;

    /**
     * 套餐ID
     */
    private Long packageId;

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
     * 过期时间开始
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTimeStart;

    /**
     * 过期时间结束
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTimeEnd;

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
    private String orderBy = "create_time";

    /**
     * 排序方向（asc/desc）
     */
    private String sortOrder = "desc";
}
