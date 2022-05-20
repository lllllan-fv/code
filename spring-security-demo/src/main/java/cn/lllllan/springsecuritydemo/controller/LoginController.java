package cn.lllllan.springsecuritydemo.controller;

import cn.lllllan.springsecuritydemo.domain.User;
import cn.lllllan.springsecuritydemo.service.LoginService;
import cn.lllllan.springsecuritydemo.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user) {
        return loginService.login(user);
    }
}
