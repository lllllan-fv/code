package cn.lllllan.demo.service.impl;

import cn.lllllan.demo.mapper.UserMapper;
import cn.lllllan.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Boolean login(String userName, String pwd) {
        return userMapper.login(userName, pwd);
    }
}
