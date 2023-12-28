package com.pumpkin.disease.filter;

import com.pumpkin.disease.security.SmsAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Enumeration;

/**
 * 短信认证过滤器
 *
 * @author wangshun
 * @date 2023/12/14
 */
public class SmsAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public static final String SPRING_SECURITY_PHONE_NO_KEY = "phoneNumber";

    public static final String SPRING_SECURITY_PHONE_CODE_KEY = "phoneCode";

    private String phoneNoParameter = SPRING_SECURITY_PHONE_NO_KEY;

    private String phoneCodeParameter = SPRING_SECURITY_PHONE_CODE_KEY;

    private boolean postOnly = true;

    public SmsAuthenticationFilter() {
        super(new AntPathRequestMatcher("/login/sms", "POST"));
    }

    public SmsAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    public SmsAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        Enumeration<String> parameterNames = request.getParameterNames();
        System.out.println(parameterNames);
        String phoneNumber = obtainPhoneNo(request);
        if (phoneNumber == null) {
            phoneNumber = "";
        } else {
            phoneNumber = phoneNumber.trim();
        }
        String phoneCode = obtainPhoneCode(request);
        if (phoneCode == null) {
            phoneCode = "";
        } else {
            phoneCode = phoneCode.trim();
        }
        SmsAuthenticationToken authRequest = new SmsAuthenticationToken(Collections.emptyList(), phoneNumber, phoneCode);
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Nullable
    protected String obtainPhoneNo(HttpServletRequest request) {
        return request.getParameter(phoneNoParameter);
    }

    @Nullable
    protected String obtainPhoneCode(HttpServletRequest request) {
        return request.getParameter(phoneCodeParameter);
    }

    protected void setDetails(HttpServletRequest request, SmsAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }
}
