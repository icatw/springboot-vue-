package com.pumpkin.disease.request.security.wallpaper;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: Pumpkin
 * @description: 用户请求入参
 * @since : 2023/3/15 21:49
 */
@Data
public class UserRegisterRequest {

    /**
     * 用户名/OpenId
     */
    @NotBlank(message = "用户名不能为空！")
    @ApiModelProperty(value = "用户名/OpenId", name = "username")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "用户密码不能为空！")
    @ApiModelProperty(value = "用户密码", name = "password")
    private String password;

    /**
     * 用户昵称
     */
    //@NotBlank(message = "用户昵称不能为为空！")
    @ApiModelProperty(value = "用户昵称", name = "nickname")
    private String nickname;

}
