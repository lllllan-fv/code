package cn.lllllan.ticket.service.impl;

import cn.lllllan.ticket.service.EmailService;
import cn.lllllan.ticket.service.VerifyCodeService;
import cn.lllllan.ticket.utils.JsonData;
import cn.lllllan.ticket.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class VerifyCodeServiceImpl implements VerifyCodeService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private EmailService emailService;

    @Override
    public Object sendVerifyCode(String to) {
        if (redisUtil.hasKey("send" + to)) {
            return JsonData.error("已发送过验证码，请稍后再试");
        }

        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; ++i) {
            code.append(random.nextInt(10));
        }

        redisUtil.set("send" + to, "yes");
        redisUtil.set("code" + to, code);
        redisUtil.expire("send" + to, 60);
        redisUtil.expire("code" + to, 120);

        StringBuilder content = new StringBuilder("注册验证码：").append(code).append("，证码五分钟内有效，请勿将验证码泄露给他人");
        emailService.sendMail(to, "ticket 验证码", content.toString());

        return JsonData.success(null, "发送成功");
    }

    @Override
    public Object checkVerifyCode(String email, String code) {
        String verifyCode = (String) redisUtil.get("code" + email);
        if (verifyCode == null) {
            return JsonData.error("验证码过期");
        } else if (!verifyCode.equals(code)) {
            return JsonData.error("验证码错误");
        } else {
            return JsonData.success(null, "验证通过");
        }
    }
}
