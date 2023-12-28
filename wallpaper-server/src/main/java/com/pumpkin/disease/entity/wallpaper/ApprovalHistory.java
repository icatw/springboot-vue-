package com.pumpkin.disease.entity.wallpaper;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 审批记录表(ApprovalHistory)实体类
 *
 * @author icatw
 * @since 2023-12-11 08:31:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("approval_history")
@ApiModel("ApprovalHistory")
public class ApprovalHistory implements Serializable {
    private static final long serialVersionUID = -25448938774786275L;
    /**
     * 主键自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 模块类型，例如 wallpaper（壁纸）、其他模块类型
     */
    @TableField(value = "module_type")
    @ApiModelProperty("模块类型，例如 wallpaper（壁纸）、其他模块类型")
    private String moduleType;

    /**
     * 模块数据的id，关联对应模块的主键
     */
    @TableField(value = "module_id")
    @ApiModelProperty("模块数据的id，关联对应模块的主键")
    private Long moduleId;

    /**
     * 提交人id
     */
    @TableField(value = "submitter_id")
    @ApiModelProperty("提交人id")
    private String submitterId;

    /**
     * 审批用户id
     */
    @TableField(value = "approval_user_id")
    @ApiModelProperty("审批用户id")
    private String approvalUserId;

    /**
     * 审批状态（0提交、1通过、2驳回）
     */
    @TableField(value = "approval_status")
    @ApiModelProperty("审批状态（0提交、1通过、2驳回）")
    private Integer approvalStatus;

    /**
     * 审批时间
     */
    @TableField(value = "approval_time")
    @ApiModelProperty("审批时间")
    private Date approvalTime;

    /**
     * 审批备注
     */
    @TableField(value = "remarks")
    @ApiModelProperty("审批备注")
    private String remarks;
    @ApiModelProperty("逻辑删除 （0 正常  1 删除）")
    @TableLogic
    private Integer isDeleted;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtUpdate;
    /**
     * 业务实体
     */
    @TableField(exist = false)
    @ApiModelProperty("业务实体")
    private Image businessEntity;
    /**
     * 提交人姓名
     */
    @TableField(exist = false)
    private String submitterName;
}

