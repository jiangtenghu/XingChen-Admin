package com.admin.identity.domain.entity;

import com.admin.common.web.domain.WebBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

/**
 * 组织架构实体类
 *
 * @author admin
 * @since 2024-01-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_organization", autoResultMap = true)
public class Organization extends WebBaseEntity {

    /**
     * 组织ID
     */
    @TableId(value = "id", type = IdType.AUTO)
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
     * 组织类型(COMPANY/DEPT/TEAM/GROUP)
     */
    private String orgType;

    /**
     * 父组织ID(0表示顶级)
     */
    private Long parentId;

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
     * 副负责人ID
     */
    private Long deputyLeaderId;

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
     * 组织状态(0-正常,1-停用)
     */
    private String status;

    /**
     * 组织配置JSON
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> orgConfig;

    /**
     * 子组织列表（用于树形结构）
     */
    @TableField(exist = false)
    private List<Organization> children;

    /**
     * 负责人名称（关联查询）
     */
    @TableField(exist = false)
    private String leaderName;

    /**
     * 成员数量（统计查询）
     */
    @TableField(exist = false)
    private Integer memberCount;

    /**
     * 组织类型枚举
     */
    public enum OrgType {
        COMPANY("公司"),
        DEPT("部门"),
        TEAM("团队"),
        GROUP("小组");

        private final String description;

        OrgType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 组织状态枚举
     */
    public enum OrgStatus {
        NORMAL("0", "正常"),
        DISABLED("1", "停用");

        private final String code;
        private final String description;

        OrgStatus(String code, String description) {
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
