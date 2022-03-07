package cn.lllllan.demo.controller;

import cn.lllllan.demo.domain.User;
import cn.lllllan.demo.utils.JsonData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pub/user")
public class UserController {

    @PostMapping("login")
    public Object login(String userName, String pwd) {
        return JsonData.buildSuccess(new User(userName, pwd));
    }
}
