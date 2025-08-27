package com.admin.identity.domain.entity;

import com.admin.common.web.domain.WebBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户实体类
 *
 * @author admin
 * @since 2024-01-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class User extends WebBaseEntity {

    /**
     * 用户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 用户账号
     */
    private String username;

    /**
     * 密码(认证服务管理)
     */
    private String password;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 真实姓名
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
     * 用户性别(0-男,1-女,2-未知)
     */
    private String sex;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 地址
     */
    private String address;

    /**
     * 员工工号
     */
    private String employeeNo;

    /**
     * 入职日期
     */
    private LocalDate entryDate;

    /**
     * 职位
     */
    private String position;

    /**
     * 直属上级ID
     */
    private Long superiorId;

    /**
     * 用户类型(ADMIN/NORMAL/GUEST)
     */
    private String userType;

    /**
     * 账号状态(0-正常,1-停用,2-删除)
     */
    private String status;

    /**
     * 最后登录时间
     */
    private LocalDateTime loginDate;

    /**
     * 最后登录IP
     */
    private String loginIp;

    /**
     * 用户类型枚举
     */
    public enum UserType {
        ADMIN("管理员"),
        NORMAL("普通用户"),
        GUEST("访客");

        private final String description;

        UserType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 用户状态枚举
     */
    public enum UserStatus {
        NORMAL("0", "正常"),
        DISABLED("1", "停用"),
        DELETED("2", "删除");

        private final String code;
        private final String description;

        UserStatus(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 性别枚举
     */
    public enum Gender {
        MALE("0", "男"),
        FEMALE("1", "女"),
        UNKNOWN("2", "未知");

        private final String code;
        private final String description;

        Gender(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }
}
