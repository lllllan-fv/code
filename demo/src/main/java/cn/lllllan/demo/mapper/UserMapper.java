package cn.lllllan.demo.mapper;

import cn.lllllan.demo.domain.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserMapper {

    private static Map<String, User> userMap = new HashMap<>();

    static {
        userMap.put("TheShy", new User(1, "TheShy", "123"));
        userMap.put("Rookie", new User(1, "Rookie", "123"));
        userMap.put("Ning", new User(1, "Ning", "123"));
    }

    public Boolean login(String userName, String pwd) {
        User user = userMap.get(userName);
        return user != null && user.getPwd().equals(pwd);
    }
}
