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
 * 壁纸表(Image)实体类
 *
 * @author icatw
 * @since 2022-10-14 19:58:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "壁纸对象", description = "壁纸表")
@TableName(value = "wallpaper_image")
public class Image implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键自增id
     */
    @ApiModelProperty("壁纸主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 壁纸名字
     */
    @ApiModelProperty("壁纸名字")
    private String imageName;
    @ApiModelProperty("流程id")
    private String approvalId;
    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String description;

    /**
     * 壁纸url
     */
    @ApiModelProperty("壁纸url")
    private String imageUrl;

    /**
     * 图片分辨率(width*heigh)
     */
    @ApiModelProperty("图片分辨率(width*heigh)")
    private String imageSize;
    /**
     * 文件大小（KB）
     */
    @ApiModelProperty("图片大小（KB）")
    private String fileSize;
    /**
     * 上传人id
     */
    @ApiModelProperty("上传人id")
    private String uploadBy;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date updateTime;

    /**
     * 分类id
     */
    @ApiModelProperty("分类id")
    private Long typeId;
    /**
     * 分类id
     */
    @ApiModelProperty("下载数")
    private Long downloads;
    @ApiModelProperty("逻辑删除 （0 正常  1 删除）")
    @TableLogic
    private Integer isDeleted;
    @ApiModelProperty("审核状态（0未提交、1待审核、2审核通过、3审核不通过、4驳回、5撤回）")
    private Integer status;
    /**
     * 类型名称
     */
    @TableField(exist = false)
    @ApiModelProperty("分类名")
    private String typeName;
}

