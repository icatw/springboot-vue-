package com.pumpkin.disease.handler;

import com.alibaba.fastjson.JSON;
import com.pumpkin.disease.base.result.ResponseResult;
import com.pumpkin.disease.service.LoginLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.pumpkin.disease.constant.SystemConstant.Charset.JSON_TYPE_UTF8_CHARSET;

/**
 * 短信身份验证失败处理程序impl
 *
 * @author wangshun
 * @date 2023/12/14
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SmsAuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {

    private final LoginLogService loginLogService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType(JSON_TYPE_UTF8_CHARSET);
        loginLogService.insertNewLoginLog(request, false, exception.getMessage());
        response.getWriter().write(JSON.toJSONString(ResponseResult.fail(exception.getMessage())));
    }
}
