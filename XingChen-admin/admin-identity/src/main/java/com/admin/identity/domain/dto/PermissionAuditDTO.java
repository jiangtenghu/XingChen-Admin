package com.admin.identity.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * 权限审计DTO
 *
 * @author XingChen
 * @date 2024-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "PermissionAuditDTO", description = "权限审计记录")
public class PermissionAuditDTO {

    @Schema(description = "审计ID", example = "1")
    private Long auditId;

    @Schema(description = "租户ID", example = "1")
    private Long tenantId;

    @Schema(description = "租户名称", example = "默认租户")
    private String tenantName;

    @Schema(description = "操作类型", example = "GRANT_PERMISSION", allowableValues = {
        "GRANT_PERMISSION", "REVOKE_PERMISSION", "CREATE_ROLE", "DELETE_ROLE", 
        "ASSIGN_ROLE", "REMOVE_ROLE", "CREATE_USER", "DELETE_USER", "LOGIN", "LOGOUT"
    })
    private String operationType;

    @Schema(description = "操作类型名称", example = "授予权限")
    private String operationTypeName;

    @Schema(description = "操作对象类型", example = "USER", allowableValues = {"USER", "ROLE", "PERMISSION", "ORGANIZATION"})
    private String targetType;

    @Schema(description = "操作对象ID", example = "1001")
    private Long targetId;

    @Schema(description = "操作对象名称", example = "张三")
    private String targetName;

    @Schema(description = "权限ID", example = "1")
    private Long permissionId;

    @Schema(description = "权限名称", example = "用户管理")
    private String permissionName;

    @Schema(description = "权限标识", example = "user:manage")
    private String permissionKey;

    @Schema(description = "角色ID", example = "1")
    private Long roleId;

    @Schema(description = "角色名称", example = "系统管理员")
    private String roleName;

    @Schema(description = "操作人ID", example = "1")
    private Long operatorId;

    @Schema(description = "操作人用户名", example = "admin")
    private String operatorUsername;

    @Schema(description = "操作人真实姓名", example = "管理员")
    private String operatorRealName;

    @Schema(description = "操作时间", example = "2024-08-26T10:30:00")
    private LocalDateTime operationTime;

    @Schema(description = "操作IP", example = "192.168.1.100")
    private String operationIp;

    @Schema(description = "操作地点", example = "北京市朝阳区")
    private String operationLocation;

    @Schema(description = "用户代理", example = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
    private String userAgent;

    @Schema(description = "操作结果", example = "SUCCESS", allowableValues = {"SUCCESS", "FAILED", "PARTIAL_SUCCESS"})
    private String operationResult;

    @Schema(description = "操作描述", example = "为用户张三授予用户管理权限")
    private String operationDescription;

    @Schema(description = "操作前状态", example = "用户没有用户管理权限")
    private String beforeState;

    @Schema(description = "操作后状态", example = "用户拥有用户管理权限")
    private String afterState;

    @Schema(description = "变更详情", example = "新增权限: user:manage")
    private String changeDetails;

    @Schema(description = "风险等级", example = "MEDIUM", allowableValues = {"LOW", "MEDIUM", "HIGH", "CRITICAL"})
    private String riskLevel;

    @Schema(description = "风险评分", example = "6.5")
    private Double riskScore;

    @Schema(description = "审计规则ID", example = "1")
    private Long auditRuleId;

    @Schema(description = "审计规则名称", example = "敏感权限变更监控")
    private String auditRuleName;

    @Schema(description = "是否敏感操作", example = "true")
    private Boolean isSensitiveOperation;

    @Schema(description = "是否需要审批", example = "true")
    private Boolean requiresApproval;

    @Schema(description = "审批状态", example = "APPROVED", allowableValues = {"PENDING", "APPROVED", "REJECTED", "NOT_REQUIRED"})
    private String approvalStatus;

    @Schema(description = "审批人ID", example = "2")
    private Long approverId;

    @Schema(description = "审批人姓名", example = "李四")
    private String approverName;

    @Schema(description = "审批时间", example = "2024-08-26T11:00:00")
    private LocalDateTime approvalTime;

    @Schema(description = "审批意见", example = "同意授权")
    private String approvalComment;

    @Schema(description = "会话ID", example = "session_123456")
    private String sessionId;

    @Schema(description = "请求ID", example = "req_789012")
    private String requestId;

    @Schema(description = "业务ID", example = "biz_345678")
    private String businessId;

    @Schema(description = "关联审计ID", example = "1000")
    private Long relatedAuditId;

    @Schema(description = "数据分类", example = "PERSONAL_DATA", allowableValues = {"PUBLIC", "INTERNAL", "CONFIDENTIAL", "PERSONAL_DATA"})
    private String dataClassification;

    @Schema(description = "合规标签", example = "[\"GDPR\", \"SOX\"]")
    private String complianceTags;

    @Schema(description = "保留期限（天）", example = "2555")
    private Integer retentionDays;

    @Schema(description = "是否已归档", example = "false")
    private Boolean isArchived;

    @Schema(description = "归档时间", example = "2024-12-31T23:59:59")
    private LocalDateTime archivedTime;

    @Schema(description = "扩展属性", example = "{\"department\": \"技术部\", \"project\": \"用户管理系统\"}")
    private String extraData;

    @Schema(description = "检索关键词", example = "用户管理,权限授予,张三")
    private String searchKeywords;

    @Schema(description = "创建时间", example = "2024-08-26T10:30:00")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", example = "2024-08-26T11:00:00")
    private LocalDateTime updateTime;
}

