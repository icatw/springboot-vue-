package com.pumpkin.disease.entity.wallpaper;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 壁纸分类表(Type)实体类
 *
 * @author icatw
 * @since 2022-10-14 19:58:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("Type")
@TableName(value="wallpaper_type")
public class Type implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 分类名
     */
    @ApiModelProperty("分类名")
    private String typeName;
    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String description;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 状态(是否启用：0-禁用，1-启用)
     */
    @ApiModelProperty("状态(是否启用：0-禁用，1-启用)")
    private Integer status;
    @ApiModelProperty("逻辑删除 （0 正常  1 删除）")
    @TableLogic
    private Integer isDeleted;
    /**
     * 分类下壁纸数量
     */
    @ApiModelProperty("分类下壁纸数量")
    @TableField(exist = false)
    private Integer imageNum;
}

