package com.pumpkin.disease.security;

import cn.hutool.core.util.StrUtil;
import com.google.common.base.VerifyException;
import com.pumpkin.disease.constant.SystemConstant;
import com.pumpkin.disease.enums.http.HttpCodeEnum;
import com.pumpkin.disease.request.security.user.UserRequest;
import com.pumpkin.disease.service.UserAuthService;
import com.pumpkin.disease.service.UserService;
import com.pumpkin.disease.service.impl.UserDetailsServiceImpl;
import com.pumpkin.disease.utils.sms.SmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 短信认证提供商
 *
 * @author wangshun
 * @date 2023/12/14
 */
//@Component
@Slf4j
public class SmsAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserAuthService userAuthService;
    @Autowired
    private SmsUtil smsUtil;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取authentication参数的principal属性作为手机号
        String phoneNo = authentication.getPrincipal().toString();
        if (StrUtil.isEmpty(phoneNo)) {
            log.error("phoneNo cannot be null");
            throw new BadCredentialsException("phoneNo cannot be null");
        }

        // 获取authentication参数的credentials属性作为短信验证码
        String phoneCode = authentication.getCredentials().toString();
        if (StrUtil.isEmpty(phoneCode)) {
            log.error("phoneCode cannot be null");
            throw new BadCredentialsException("phoneCode cannot be null");
        }
        // 根据手机号组成的key值去redis缓存中查询发送短信验证码时存储的验证码

        try {
            if (!smsUtil.verifyCode(phoneNo, phoneCode)) {
                log.error("验证码错误，手机号：{}", phoneNo);
                throw new VerifyException(HttpCodeEnum.CAPTCHA_ERROR.getMessage());
            }
        } catch (Exception e) {
            log.error("验证码验证过程中发生异常，手机号：{}", phoneNo, e);
            throw new BadCredentialsException(e.getMessage());
        }
        //先检查用户名/手机号是否存在
        if (!userAuthService.checkUsername(phoneNo)) {
            UserRequest request = new UserRequest();
            request.setUsername(phoneNo);
            request.setPassword(SystemConstant.User.DEFAULT_PASSWORD);
            userService.saveUser(request);
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(phoneNo);
        SmsAuthenticationToken authenticationResult = new SmsAuthenticationToken(userDetails.getAuthorities(), userDetails, phoneCode);
        authenticationResult.setDetails(authentication.getDetails());

        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
