package com.pumpkin.disease.base.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author 王顺
 * @date 2023/12/14
 * @email 762188827@qq.com
 * @apiNote
 */
public class SmsAuthenticationException extends AuthenticationException {
    public SmsAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public SmsAuthenticationException(String msg) {
        super(msg);
    }
}
