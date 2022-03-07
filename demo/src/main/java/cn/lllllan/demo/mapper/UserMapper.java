package cn.lllllan.demo.mapper;

import cn.lllllan.demo.domain.User;

import java.util.HashMap;
import java.util.Map;

public class UserMapper {

    private static Map<Integer, User> userMap = new HashMap<>();

    static {
        userMap.put(1, new User(1, "TheShy", "123"));
        userMap.put(1, new User(1, "Rookie", "123"));
        userMap.put(1, new User(1, "Ning", "123"));
    }
}
