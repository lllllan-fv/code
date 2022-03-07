package cn.lllllan.demo.mapper;

import cn.lllllan.demo.domain.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserMapper {

    private static Map<String, User> userMap = new HashMap<>();

    static {
        userMap.put("TheShy", new User(1, "TheShy", "123"));
        userMap.put("Rookie", new User(2, "Rookie", "123"));
        userMap.put("Ning", new User(3, "Ning", "123"));
    }

    public User login(String userName, String pwd) {
        return userMap.get(userName);
    }

    public List<User> listUser() {
        return new ArrayList<User>(userMap.values());
    }
}
