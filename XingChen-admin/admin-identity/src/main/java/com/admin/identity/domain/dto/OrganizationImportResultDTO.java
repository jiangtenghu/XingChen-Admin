package com.admin.identity.domain.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 组织导入结果DTO
 *
 * @author admin
 * @since 2024-01-15
 */
@Data
public class OrganizationImportResultDTO {

    /**
     * 总导入数量
     */
    private Integer totalCount;

    /**
     * 成功数量
     */
    private Integer successCount;

    /**
     * 失败数量
     */
    private Integer failureCount;

    /**
     * 成功的组织列表
     */
    private List<OrganizationResponseDTO> successOrganizations = new ArrayList<>();

    /**
     * 失败的记录列表
     */
    private List<ImportFailureRecord> failureRecords = new ArrayList<>();

    /**
     * 导入失败记录
     */
    @Data
    public static class ImportFailureRecord {
        /**
         * 组织名称
         */
        private String orgName;

        /**
         * 组织编码
         */
        private String orgCode;

        /**
         * 失败原因
         */
        private String errorMessage;

        /**
         * 行号
         */
        private Integer rowNumber;
    }

    /**
     * 添加成功记录
     */
    public void addSuccessOrganization(OrganizationResponseDTO organization) {
        this.successOrganizations.add(organization);
        this.successCount = this.successOrganizations.size();
    }

    /**
     * 添加失败记录
     */
    public void addFailureRecord(String orgName, String orgCode, String errorMessage, Integer rowNumber) {
        ImportFailureRecord record = new ImportFailureRecord();
        record.setOrgName(orgName);
        record.setOrgCode(orgCode);
        record.setErrorMessage(errorMessage);
        record.setRowNumber(rowNumber);
        this.failureRecords.add(record);
        this.failureCount = this.failureRecords.size();
    }

    /**
     * 计算总数
     */
    public void calculateTotal() {
        this.totalCount = this.successCount + this.failureCount;
    }
}
