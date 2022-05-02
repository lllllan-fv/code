package cn.lllllan.springdemo.controller;

import cn.lllllan.springdemo.service.UserService;
import cn.lllllan.springdemo.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/v1/pri/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("register")
    public JsonData register(@RequestBody Map<String, String> userInfo) {
//        http://localhost:8081/api/v1/pri/user/register

        int rows = userService.register(userInfo);

        return rows == 1 ? JsonData.success("注册成功") : JsonData.error("注册失败");
    }

    @PostMapping("login")
    public JsonData login(@RequestBody Map<String, String> userInfo) {
        String token = userService.login(userInfo.get("phone"), userInfo.get("pwd"));
        return token == null ? JsonData.error("账号或密码错误") : JsonData.success(token, "登录成功");
    }
}
