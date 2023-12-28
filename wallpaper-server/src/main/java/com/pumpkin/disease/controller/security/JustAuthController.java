package com.pumpkin.disease.controller.security;

import com.alibaba.fastjson.JSON;
import com.pumpkin.disease.base.result.ResponseResult;
import com.pumpkin.disease.entity.User;
import com.pumpkin.disease.enums.business.LoginTypeEnum;
import com.pumpkin.disease.request.security.user.UserRequest;
import com.pumpkin.disease.service.UserAuthService;
import com.pumpkin.disease.service.UserService;
import com.pumpkin.disease.service.impl.UserDetailsServiceImpl;
import com.pumpkin.disease.utils.jwt.JwtUtil;
import com.xkcoding.http.config.HttpConfig;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.*;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 第三方登录
 *
 * @author Tu_Yooo
 * @Date 2021/6/27 19:00
 */
@RestController
@RequestMapping("/oauth")
public class JustAuthController {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;
    @Autowired
    UserAuthService authService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * 获取授权链接并跳转到第三方授权页面
     *
     * @param response response
     * @throws IOException response可能存在的异常
     */
    @RequestMapping("/render/{source}")
    public ResponseResult renderAuth(@PathVariable("source") String source, HttpServletResponse response) throws IOException {
        AuthRequest authRequest = getAuthRequest(source);
        String token = AuthStateUtils.createState();
        //生成gitee的授权url
        String authorizeUrl = authRequest.authorize(token);
        //将这个url返回给前端Vue
        //由Vue去执行 授权页
        Map<String, String> map = new HashMap<>();
        map.put("url", authorizeUrl);
        System.out.println(authorizeUrl);
        return ResponseResult.success(map);
    }

    /**
     * 用户在确认第三方平台授权（登录）后， 第三方平台会重定向到该地址，并携带code、state等参数
     *
     * @param callback 第三方回调时的入参
     * @return 第三方平台的用户信息
     */
    @RequestMapping("/callback/{source}")
    public void login(@PathVariable("source") String source, AuthCallback callback, HttpServletResponse response) throws IOException {
        AuthRequest authRequest = getAuthRequest(source);
        AuthResponse authResponse = authRequest.login(callback);
        String s = JSON.toJSONString(authResponse.getData());
        Map<String, Object> authResponseMap = JSON.parseObject(s, Map.class);
        //先检查用户名/手机号是否存在
        String openId = (String) authResponseMap.get("uuid");
        String jwt = jwtUtil.createJwt(openId);
        System.out.println(jwt);
        if (!authService.checkByOpenId(openId, source)) {
            UserRequest request = new UserRequest();
            request.setUsername(openId);
            request.setNickname((String) authResponseMap.get("nickname"));
            request.setLoginType(LoginTypeEnum.getValueByDesc(source));
            request.setAvatar((String) authResponseMap.get("avatar"));
            userService.saveUser(request);
        }
        User user = userDetailsService.loadUserByUsernameAndLoginType(openId, LoginTypeEnum.getValueByDesc(source));
        //将登录信息放入springSecurity管理
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        //本地
        //response.sendRedirect("http://localhost:8802/#/loading?token=" + jwt);
        //部署线上
        response.sendRedirect("http://www.wallpaper.icatw.top/#/loading?token=" + jwt);
    }


    /**
     * 获取授权Request
     *
     * @return AuthRequest
     */


    /**
     * 根据具体的授权来源，获取授权请求工具类
     *
     * @param source
     * @return
     */
    private AuthRequest getAuthRequest(String source) {
        //本地
        //String callBackBaseUrl = "http://localhost:9888/oauth/callback";
        //部署线上
        String callBackBaseUrl = "ip";
        AuthRequest authRequest = null;
        switch (source) {
            case "dingtalk":
                authRequest = new AuthDingTalkRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri(callBackBaseUrl + "/dingtalk")
                        .build());
                break;
            case "baidu":
                authRequest = new AuthBaiduRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri(callBackBaseUrl + "/baidu")
                        .build());
                break;
            case "github":
                authRequest = new AuthGithubRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri(callBackBaseUrl + "/github")
                        // 针对国外平台配置代理
                        .httpConfig(HttpConfig.builder()
                                // Http 请求超时时间
                                .timeout(30000)
                                // host 和 port 请修改为开发环境的参数
                                //.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890)))
                                .build())
                        .build());
                break;
            case "gitee":
                authRequest = new AuthGiteeRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri(callBackBaseUrl + "/gitee")
                        .build());
                break;
            case "weibo":
                authRequest = new AuthWeiboRequest(AuthConfig.builder()
                        .clientId("微博的APP ID")
                        .clientSecret("微博的APP Key")
                        .redirectUri(callBackBaseUrl + "/weibo")
                        .build());
                break;
            case "coding":
                authRequest = new AuthCodingRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri(callBackBaseUrl + "/coding")
                        .build());
                break;
            //case "tencentCloud":
            //    authRequest = new AuthTencentCloudRequest(AuthConfig.builder()
            //            .clientId("")
            //            .clientSecret("")
            //            .redirectUri(callBackBaseUrl + "/tencentCloud")
            //            .build());
            //    break;
            case "oschina":
                authRequest = new AuthOschinaRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri(callBackBaseUrl + "/oschina")
                        .build());
                break;
            case "alipay":
                // 支付宝在创建回调地址时，不允许使用localhost或者127.0.0.1，所以这儿的回调地址使用的局域网内的ip
                authRequest = new AuthAlipayRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .alipayPublicKey("")
                        .redirectUri(callBackBaseUrl + "/alipay")
                        .build());
                break;
            case "qq":
                authRequest = new AuthQqRequest(AuthConfig.builder()
                        .clientId("填写QQ的APP ID")
                        .clientSecret("填写QQ的APP Key")
                        .redirectUri(callBackBaseUrl + "/qq")
                        .build());
                break;
            //case "wechat":
            //    authRequest = new AuthWeChatRequest(AuthConfig.builder()
            //            .clientId("123")
            //            .clientSecret("123")
            //            .redirectUri(callBackBaseUrl + "/wechat")
            //            .build());
            //    break;
            case "csdn":
                authRequest = new AuthCsdnRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri(callBackBaseUrl + "/csdn")
                        .build());
                break;
            case "taobao":
                authRequest = new AuthTaobaoRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri(callBackBaseUrl + "/taobao")
                        .build());
                break;
            case "google":
                authRequest = new AuthGoogleRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri(callBackBaseUrl + "/google")
                        .build());
                break;
            case "facebook":
                authRequest = new AuthFacebookRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri(callBackBaseUrl + "/facebook")
                        .build());
                break;
            case "douyin":
                authRequest = new AuthDouyinRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri(callBackBaseUrl + "/douyin")
                        .build());
                break;
            case "linkedin":
                authRequest = new AuthLinkedinRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri(callBackBaseUrl + "/linkedin")
                        .build());
                break;
            case "microsoft":
                authRequest = new AuthMicrosoftRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri(callBackBaseUrl + "/microsoft")
                        .build());
                break;
            case "mi":
                authRequest = new AuthMiRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri(callBackBaseUrl + "/mi")
                        .build());
                break;
            case "toutiao":
                authRequest = new AuthToutiaoRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri(callBackBaseUrl + "/toutiao")
                        .build());
                break;
            case "teambition":
                authRequest = new AuthTeambitionRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri(callBackBaseUrl + "/teambition")
                        .build());
                break;
            case "pinterest":
                authRequest = new AuthPinterestRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri(callBackBaseUrl + "/pinterest")
                        .build());
                break;
            case "renren":
                authRequest = new AuthRenrenRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri(callBackBaseUrl + "/teambition")
                        .build());
                break;
            case "stackoverflow":
                authRequest = new AuthStackOverflowRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri(callBackBaseUrl + "/login_success")
                        .stackOverflowKey("")
                        .build());
                break;
            case "huawei":
                authRequest = new AuthHuaweiRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri(callBackBaseUrl + "/huawei")
                        .build());
                break;
            case "wechatEnterprise":
                authRequest = new AuthHuaweiRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri(callBackBaseUrl + "/wechatEnterprise")
                        .agentId("")
                        .build());
                break;
            case "kujiale":
                authRequest = new AuthKujialeRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri(callBackBaseUrl + "/kujiale")
                        .build());
                break;
            case "gitlab":
                authRequest = new AuthGitlabRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri(callBackBaseUrl + "/gitlab")
                        .build());
                break;
            case "meituan":
                authRequest = new AuthMeituanRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri(callBackBaseUrl + "/meituan")
                        .build());
                break;
            case "eleme":
                authRequest = new AuthElemeRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri(callBackBaseUrl + "/eleme")
                        .build());
                break;

            case "twitter":
                authRequest = new AuthTwitterRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri(callBackBaseUrl + "/twitter")
                        .build());
                break;
            default:
                break;
        }
        if (null == authRequest) {
            throw new AuthException("未获取到有效的Auth配置");
        }
        return authRequest;
    }

}

