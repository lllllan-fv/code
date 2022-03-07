package cn.lllllan.demo.controller;

import cn.lllllan.demo.domain.User;
import cn.lllllan.demo.service.UserService;
import cn.lllllan.demo.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pub/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("login")
    public Object login(String userName, String pwd) {
        String token = userService.login(userName, pwd);
        return token != null ? JsonData.buildSuccess(null) : JsonData.buildError("账号密码错误");
    }

    @PostMapping("userLogin")
    public JsonData userLogin(@RequestBody User user) {
        String token = userService.login(user.getUserName(), user.getPwd());
        return token != null ? JsonData.buildSuccess(user) : JsonData.buildError(null);
    }

    @GetMapping("list")
    public JsonData list() {
        return JsonData.buildSuccess(userService.listUser());
    }

}
