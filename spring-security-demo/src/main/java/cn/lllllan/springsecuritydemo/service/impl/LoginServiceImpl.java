package cn.lllllan.springsecuritydemo.service.impl;

import cn.lllllan.springsecuritydemo.domain.LoginUser;
import cn.lllllan.springsecuritydemo.domain.User;
import cn.lllllan.springsecuritydemo.service.LoginService;
import cn.lllllan.springsecuritydemo.utils.JwtUtil;
import cn.lllllan.springsecuritydemo.utils.RedisCache;
import cn.lllllan.springsecuritydemo.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }

        //使用userid生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);

        //authenticate存入redis
        redisCache.setCacheObject("login:" + userId, loginUser);

        //把token响应给前端
        HashMap<String, String> map = new HashMap<>();
        map.put("token", jwt);

        return new ResponseResult(200, "登陆成功", map);
    }
}
