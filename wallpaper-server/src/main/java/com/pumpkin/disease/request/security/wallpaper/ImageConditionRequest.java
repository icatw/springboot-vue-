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
public class ImageConditionRequest extends BaseRequest {


    /**
     * 壁纸名（关键词）
     */
    @ApiModelProperty(value = "壁纸名（关键词）",name = "imageName")
    private String imageName;
    /**
     * 类型
     */
    @ApiModelProperty(value = "类型",name = "typeId")
    private String typeId;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序依据，0：全部，1：最热，2：最新，3：随机")
    private Integer sort;
    /**
     * 类型
     */
    @ApiModelProperty(value = "分类依据，0：全部，除0以外的作为typeId")
    private Integer type;
    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态（0未提交、1待审核、2审核通过、3审核不通过）")
    private Integer status;

}
