package com.admin.identity.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 权限审计查询DTO
 *
 * @author XingChen
 * @date 2024-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "PermissionAuditQueryDTO", description = "权限审计查询条件")
public class PermissionAuditQueryDTO {

    @Schema(description = "租户ID", example = "1")
    private Long tenantId;

    @Schema(description = "操作类型", example = "GRANT_PERMISSION", allowableValues = {
        "GRANT_PERMISSION", "REVOKE_PERMISSION", "CREATE_ROLE", "DELETE_ROLE", 
        "ASSIGN_ROLE", "REMOVE_ROLE", "CREATE_USER", "DELETE_USER", "LOGIN", "LOGOUT"
    })
    private String operationType;

    @Schema(description = "操作类型列表", example = "[\"GRANT_PERMISSION\", \"REVOKE_PERMISSION\"]")
    private List<String> operationTypes;

    @Schema(description = "操作对象类型", example = "USER", allowableValues = {"USER", "ROLE", "PERMISSION", "ORGANIZATION"})
    private String targetType;

    @Schema(description = "操作对象ID", example = "1001")
    private Long targetId;

    @Schema(description = "操作对象名称（模糊查询）", example = "张三")
    private String targetName;

    @Schema(description = "权限ID", example = "1")
    private Long permissionId;

    @Schema(description = "权限名称（模糊查询）", example = "用户管理")
    private String permissionName;

    @Schema(description = "权限标识（模糊查询）", example = "user")
    private String permissionKey;

    @Schema(description = "角色ID", example = "1")
    private Long roleId;

    @Schema(description = "角色名称（模糊查询）", example = "管理员")
    private String roleName;

    @Schema(description = "操作人ID", example = "1")
    private Long operatorId;

    @Schema(description = "操作人用户名（模糊查询）", example = "admin")
    private String operatorUsername;

    @Schema(description = "操作人真实姓名（模糊查询）", example = "管理员")
    private String operatorRealName;

    @Schema(description = "操作时间开始", example = "2024-08-01T00:00:00")
    private LocalDateTime operationTimeStart;

    @Schema(description = "操作时间结束", example = "2024-08-31T23:59:59")
    private LocalDateTime operationTimeEnd;

    @Schema(description = "操作IP", example = "192.168.1.100")
    private String operationIp;

    @Schema(description = "操作IP段（CIDR）", example = "192.168.1.0/24")
    private String operationIpRange;

    @Schema(description = "操作地点（模糊查询）", example = "北京")
    private String operationLocation;

    @Schema(description = "操作结果", example = "SUCCESS", allowableValues = {"SUCCESS", "FAILED", "PARTIAL_SUCCESS"})
    private String operationResult;

    @Schema(description = "操作结果列表", example = "[\"SUCCESS\", \"FAILED\"]")
    private List<String> operationResults;

    @Schema(description = "风险等级", example = "HIGH", allowableValues = {"LOW", "MEDIUM", "HIGH", "CRITICAL"})
    private String riskLevel;

    @Schema(description = "风险等级列表", example = "[\"HIGH\", \"CRITICAL\"]")
    private List<String> riskLevels;

    @Schema(description = "最小风险评分", example = "5.0")
    private Double minRiskScore;

    @Schema(description = "最大风险评分", example = "10.0")
    private Double maxRiskScore;

    @Schema(description = "是否敏感操作", example = "true")
    private Boolean isSensitiveOperation;

    @Schema(description = "是否需要审批", example = "true")
    private Boolean requiresApproval;

    @Schema(description = "审批状态", example = "APPROVED", allowableValues = {"PENDING", "APPROVED", "REJECTED", "NOT_REQUIRED"})
    private String approvalStatus;

    @Schema(description = "审批状态列表", example = "[\"PENDING\", \"APPROVED\"]")
    private List<String> approvalStatuses;

    @Schema(description = "审批人ID", example = "2")
    private Long approverId;

    @Schema(description = "会话ID", example = "session_123456")
    private String sessionId;

    @Schema(description = "请求ID", example = "req_789012")
    private String requestId;

    @Schema(description = "业务ID", example = "biz_345678")
    private String businessId;

    @Schema(description = "数据分类", example = "PERSONAL_DATA", allowableValues = {"PUBLIC", "INTERNAL", "CONFIDENTIAL", "PERSONAL_DATA"})
    private String dataClassification;

    @Schema(description = "合规标签", example = "GDPR")
    private String complianceTags;

    @Schema(description = "是否已归档", example = "false")
    private Boolean isArchived;

    @Schema(description = "关键词搜索（模糊查询）", example = "用户管理")
    private String keywords;

    @Schema(description = "扩展字段搜索", example = "{\"department\": \"技术部\"}")
    private String extraDataSearch;

    @Schema(description = "是否包含详细信息", example = "true")
    private Boolean includeDetails = false;

    @Schema(description = "是否包含关联数据", example = "true")
    private Boolean includeRelatedData = false;

    @Schema(description = "导出格式", example = "EXCEL", allowableValues = {"EXCEL", "CSV", "PDF", "JSON"})
    private String exportFormat;

    @Schema(description = "时间分组", example = "HOUR", allowableValues = {"HOUR", "DAY", "WEEK", "MONTH"})
    private String timeGroupBy;

    @Schema(description = "统计维度", example = "[\"operationType\", \"riskLevel\"]")
    private List<String> statisticsDimensions;

    @Schema(description = "页码", example = "1")
    private Integer pageNum = 1;

    @Schema(description = "每页大小", example = "20")
    private Integer pageSize = 20;

    @Schema(description = "排序字段", example = "operationTime")
    private String orderBy = "operationTime";

    @Schema(description = "排序方向", example = "desc", allowableValues = {"asc", "desc"})
    private String orderDirection = "desc";
}

