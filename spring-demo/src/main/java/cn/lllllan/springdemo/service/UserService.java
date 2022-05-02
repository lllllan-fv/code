package cn.lllllan.springdemo.service;

import cn.lllllan.springdemo.domain.User;

import java.util.Map;

public interface UserService {

    int register(Map<String, String> userInfo);

    String login(String phone, String pwd);

    User findUserByPhone(String phone);
}
