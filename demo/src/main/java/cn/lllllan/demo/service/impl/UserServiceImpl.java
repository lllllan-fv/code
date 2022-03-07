package cn.lllllan.demo.service.impl;

import cn.lllllan.demo.domain.User;
import cn.lllllan.demo.mapper.UserMapper;
import cn.lllllan.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    public static Map<String, User> sessionMap = new HashMap<>();

    @Autowired
    private UserMapper userMapper;

    @Override
    public String login(String userName, String pwd) {
        User user = userMapper.login(userName, pwd);

        if (user == null) {
            return null;
        } else {
            String token = UUID.randomUUID().toString();
            System.out.println("token = " + token);
            sessionMap.put(token, user);
            return token;
        }
    }

    @Override
    public List<User> listUser() {
        return userMapper.listUser();
    }
}
