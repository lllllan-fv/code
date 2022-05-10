package cn.lllllan.springdemo.mapper;

import cn.lllllan.springdemo.domain.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    int register(User user);

    User login(@Param("phone") String phone, @Param("pwd") String pwd);

    User findUserByPhone(@Param("phone") String phone);

}
