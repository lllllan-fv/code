package cn.lllllan.springsecuritydemo.service;

import cn.lllllan.springsecuritydemo.domain.User;
import cn.lllllan.springsecuritydemo.utils.ResponseResult;

public interface LoginService {

    public ResponseResult login(User user);
}
