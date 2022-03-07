package cn.lllllan.demo.service;

import cn.lllllan.demo.domain.User;

import java.util.List;

public interface UserService {
    String login(String userName, String pwd);

    List<User> listUser();
}
