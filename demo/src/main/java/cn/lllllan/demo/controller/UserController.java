package cn.lllllan.demo.controller;

import cn.lllllan.demo.domain.User;
import cn.lllllan.demo.service.UserService;
import cn.lllllan.demo.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pub/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("login")
    public Object login(String userName, String pwd) {
        return userService.login(userName, pwd) ? JsonData.buildSuccess(null) : JsonData.buildError("账号密码错误");
    }

    @PostMapping("userLogin")
    public Object userLogin(@RequestBody User user) {
        return JsonData.buildSuccess(user);
    }
}
