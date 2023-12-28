package com.pumpkin.disease.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 手机认证令牌
 *
 * @author wangshun
 * @date 2023/12/14
 */
public class SmsAuthenticationToken extends AbstractAuthenticationToken {
    /**
     * 登录身份，这里是手机号
     */
    private Object principal;


    /**
     * 登录凭证，这里是短信验证码
     */
    private Object credentials;

    /**
     * 构造方法
     *
     * @param authorities 权限集合
     * @param principal   登录身份
     * @param credentials 登录凭据
     */
    public SmsAuthenticationToken(Collection<? extends GrantedAuthority> authorities, Object principal, Object credentials) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    // 不允许通过set方法设置认证标识
    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    // 擦除登录凭据
    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        credentials = null;
    }

    // 获取认证token的名字
    @Override
    public String getName() {
        return "mobilePhoneAuthenticationToken";
    }
}
