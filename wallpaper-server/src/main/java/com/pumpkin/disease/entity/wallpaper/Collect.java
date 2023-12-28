package com.pumpkin.disease.entity.wallpaper;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 收藏表(Collect)实体类
 *
 * @author icatw
 * @since 2022-10-16 01:57:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "用户收藏",description = "用户收藏表")
@TableName(value = "wallpaper_collect")
public class Collect implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private String userId;

    /**
     * 壁纸id
     */
    @ApiModelProperty("壁纸id")
    private Integer imageId;

    @ApiModelProperty("收藏状态")
    private Integer status;
}

