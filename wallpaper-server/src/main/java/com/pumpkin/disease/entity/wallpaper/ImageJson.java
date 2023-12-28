package com.pumpkin.disease.entity.wallpaper;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 图像json
 *
 * @author icatw
 * @date 2022/10/16
 * @email 762188827@qq.com
 * @apiNote
 */
@Getter
@Setter
@Builder
public class ImageJson {
    /**
     * 响应码
     */
    private String code;
    /**
     * imgurl
     */
    private String imgurl;
    /**
     * 宽度
     */
    private String width;
    /**
     * 身高
     */
    private String height;
    /**
     * 格式
     */
    private String format;
    /**
     * 类别
     */
    private String category;
}
