package com.pumpkin.disease.request.security.wallpaper;

import com.pumpkin.disease.base.request.BaseRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: Pumpkin
 * @description: 用户条件请求入参
 * @since : 2023/3/13
 */
@Getter
@Setter
public class TypeConditionRequest extends BaseRequest {


    /**
     * 分类名（关键词）
     */
    @ApiModelProperty(value = "分类名（关键词）",name = "imageName")
    private String typeName;


    //TODO 设备类型、注册来源 待扩展

}
