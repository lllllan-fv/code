package cn.lllllan.ticket.controller;

import cn.lllllan.ticket.service.VerifyCodeService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/verify")
public class VerifyCodeController {

    @Autowired
    private VerifyCodeService verifyCodeService;

    @RequestMapping("send")
    public Object sendVerifyCode(String email) {
        return verifyCodeService.sendVerifyCode(email);
    }

    @RequestMapping("check")
    public Object checkVerifyCode(@Param("email") String email, @Param("code") String code) {
        return verifyCodeService.checkVerifyCode(email, code);
    }

}
