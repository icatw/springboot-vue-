package com.pumpkin.disease.controller.system;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.RandomUtil;
import com.aliyuncs.exceptions.ClientException;
import com.pumpkin.disease.annotation.AnonymousAccess;
import com.pumpkin.disease.base.result.ResponseResult;
import com.pumpkin.disease.utils.sms.SmsUtil;
import com.pumpkin.disease.utils.vaild.AssertUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短信控制器
 *
 * @author wangshun
 * @date 2023/12/14
 */
@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
public class SmsController {

    private final SmsUtil smsUtil;

    @GetMapping("/sendCode")
    @AnonymousAccess
    public ResponseResult<String> sendCode(@RequestParam String phoneNumber) throws ClientException {
        AssertUtil.assertIsNotPass(!Validator.isMobile(phoneNumber), "手机号有误！");
        // 生成验证码（这里需要你自己实现）
        //String code = generateVerificationCode();
        String code = RandomUtil.randomNumbers(6);
        // 发送短信
        smsUtil.sendCode(phoneNumber, code);
        return ResponseResult.success("短信发送成功！");
    }
}
