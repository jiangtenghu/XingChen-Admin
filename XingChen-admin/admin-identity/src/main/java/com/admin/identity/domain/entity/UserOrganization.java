package com.admin.identity.domain.entity;

import com.admin.common.web.domain.WebBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户组织关联实体类
 *
 * @author admin
 * @since 2024-01-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user_organization")
public class UserOrganization extends WebBaseEntity {

    /**
     * 关联ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 组织ID
     */
    private Long orgId;

    /**
     * 职位名称
     */
    private String position;

    /**
     * 职级
     */
    private String jobLevel;

    /**
     * 职等
     */
    private String jobGrade;

    /**
     * 工作地点
     */
    private String workLocation;

    /**
     * 是否主要组织
     */
    private Boolean isPrimary;

    /**
     * 是否负责人
     */
    private Boolean isLeader;

    /**
     * 是否副负责人
     */
    private Boolean isDeputyLeader;

    /**
     * 工作比例(用于兼职)
     */
    private BigDecimal workRatio;

    /**
     * 加入时间
     */
    private LocalDateTime joinTime;

    /**
     * 离开时间
     */
    private LocalDateTime leaveTime;

    /**
     * 生效时间
     */
    private LocalDateTime effectiveTime;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 分配状态(0-正常,1-停用,2-离职)
     */
    private String status;

    /**
     * 分配原因
     */
    private String assignReason;

    /**
     * 离开原因
     */
    private String leaveReason;

    /**
     * 用户信息（关联查询）
     */
    @TableField(exist = false)
    private User user;

    /**
     * 组织信息（关联查询）
     */
    @TableField(exist = false)
    private Organization organization;

    /**
     * 分配状态枚举
     */
    public enum AssignmentStatus {
        NORMAL("0", "正常"),
        DISABLED("1", "停用"),
        RESIGNED("2", "离职");

        private final String code;
        private final String description;

        AssignmentStatus(String code, String description) {
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
