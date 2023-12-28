package com.pumpkin.disease.entity.wallpaper;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author icatw
 * @date 2022/10/29
 * @email 762188827@qq.com
 * @apiNote
 */
@Data
public class ImageVo {
    /**
     * 主键自增id
     */
    private Long id;

    /**
     * 壁纸名字
     */
    @ApiModelProperty("壁纸名字")
    private String imageName;

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
     * 收藏状态 0：未收藏 1：已收藏
     */
    private Integer status = 0;
}
