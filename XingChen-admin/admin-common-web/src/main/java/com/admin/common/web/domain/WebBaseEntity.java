package com.admin.common.web.domain;

import com.admin.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Web 服务基础实体类 - 包含 MyBatis Plus 注解
 */
@Schema(description = "Web 服务基础实体")
public class WebBaseEntity extends BaseEntity {

    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private java.time.LocalDateTime createTime;

    @Schema(description = "更新人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private java.time.LocalDateTime updateTime;

    @Schema(description = "删除标志（0代表存在 1代表删除）")
    @TableLogic
    private Integer delFlag;

    // 重写父类的 getter/setter 方法
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
        super.setId(id);
    }

    @Override
    public String getCreateBy() {
        return createBy;
    }

    @Override
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
        super.setCreateBy(createBy);
    }

    @Override
    public java.time.LocalDateTime getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(java.time.LocalDateTime createTime) {
        this.createTime = createTime;
        super.setCreateTime(createTime);
    }

    @Override
    public String getUpdateBy() {
        return updateBy;
    }

    @Override
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
        super.setUpdateBy(updateBy);
    }

    @Override
    public java.time.LocalDateTime getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(java.time.LocalDateTime updateTime) {
        this.updateTime = updateTime;
        super.setUpdateTime(updateTime);
    }

    @Override
    public Integer getDelFlag() {
        return delFlag;
    }

    @Override
    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
        super.setDelFlag(delFlag);
    }
}