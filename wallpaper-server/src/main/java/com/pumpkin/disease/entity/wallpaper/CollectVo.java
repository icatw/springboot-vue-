package com.pumpkin.disease.entity.wallpaper;

import lombok.Data;

/**
 * @author icatw
 * @date 2022/10/16
 * @email 762188827@qq.com
 * @apiNote
 */
@Data
public class CollectVo {
    private Integer imageId;
    private Integer status = 1;
    private String imageSize;
    private String imageName;
    private String imageUrl;
}
