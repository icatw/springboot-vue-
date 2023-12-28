package com.pumpkin.disease.utils.sms;

import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.RandomUtil;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.pumpkin.disease.enums.http.HttpCodeEnum;
import com.pumpkin.disease.utils.redis.RedisCache;
import com.pumpkin.disease.utils.vaild.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 短信实用程序
 *
 * @author 76218
 * @date 2023/12/14
 */
@Component
@Slf4j
public class SmsUtil {

    @Value("${aliyun.sms.access-key-id}")
    private String accessKeyId;

    @Value("${aliyun.sms.access-key-secret}")
    private String accessKeySecret;

    @Value("${aliyun.sms.sign-name}")
    private String signName;

    @Value("${aliyun.sms.template-code}")
    private String templateCode;
    @Resource
    private RedisCache redisCache;
    private final String KEY = "sms_code:";

    public void sendCode(String phoneNumber, String code) throws ClientException {
        //redis键
        String key = KEY + phoneNumber;
        String cacheCode = redisCache.getCacheObject(key);
        AssertUtil.assertIsNotPass(AssertUtil.isNotNull(cacheCode), StrFormatter.format("您当前手机号:{}已发送过短信，请勿重复发送！", phoneNumber));
        DefaultProfile profile = DefaultProfile.getProfile(
                "your-region-id",  // 替换成你的阿里云Region ID
                accessKeyId,
                accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);

        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phoneNumber);
        request.setSignName(signName);
        request.setTemplateCode(templateCode);
        request.setTemplateParam("{\"code\":\"" + code + "\"}");

        SendSmsResponse response = client.getAcsResponse(request);
        // 处理发送结果
        System.out.println("短信发送结果：" + response.getCode() + ", " + response.getMessage());
        log.info("验证码：" + code);
        // 将验证码保存到 Redis，有效期设置为五分钟

        redisCache.setCacheObject(key, code, 5, TimeUnit.MINUTES);
    }

    public boolean verifyCode(String phoneNumber, String enteredCode) {
        // 从 Redis 获取保存的验证码
        String key = KEY + phoneNumber;
        String cacheCode = redisCache.getCacheObject(key);
        AssertUtil.assertIsNotPass(AssertUtil.isEmpty(cacheCode), HttpCodeEnum.CAPTCHA_ALREADY_EXPIRE.getMessage());
        // 验证用户输入的验证码是否正确
        return enteredCode.equals(cacheCode);
    }

    private String generateVerificationCode() {
        // 实现生成验证码的逻辑，例如随机生成6位数字
        return RandomUtil.randomNumbers(6);
    }
}
