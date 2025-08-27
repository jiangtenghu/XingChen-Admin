package com.admin.identity.domain.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户查询DTO
 *
 * @author admin
 * @since 2024-01-15
 */
@Data
public class UserQueryDTO {

    /**
     * 用户账号（模糊查询）
     */
    private String username;

    /**
     * 用户昵称（模糊查询）
     */
    private String nickname;

    /**
     * 真实姓名（模糊查询）
     */
    private String realName;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 用户性别
     */
    private String sex;

    /**
     * 员工工号
     */
    private String employeeNo;

    /**
     * 职位（模糊查询）
     */
    private String position;

    /**
     * 直属上级ID
     */
    private Long superiorId;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 用户来源
     */
    private String userSource;

    /**
     * 用户标签
     */
    private String userTags;

    /**
     * 账号状态
     */
    private String status;

    /**
     * 组织ID
     */
    private Long orgId;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 入职日期开始
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate entryDateStart;

    /**
     * 入职日期结束
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate entryDateEnd;

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
     * 最后登录时间开始
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime loginDateStart;

    /**
     * 最后登录时间结束
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime loginDateEnd;

    /**
     * 是否包含子组织用户
     */
    private Boolean includeSubOrgs = false;

    /**
     * 组织ID列表（用于批量查询）
     */
    private List<Long> orgIds;

    /**
     * 角色ID列表（用于批量查询）
     */
    private List<Long> roleIds;

    /**
     * 用户ID列表（用于批量查询）
     */
    private List<Long> userIds;

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
