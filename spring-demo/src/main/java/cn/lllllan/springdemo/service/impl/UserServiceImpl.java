package cn.lllllan.springdemo.service.impl;

import cn.lllllan.springdemo.domain.User;
import cn.lllllan.springdemo.mapper.UserMapper;
import cn.lllllan.springdemo.service.UserService;
import cn.lllllan.springdemo.utils.MD5Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 放在CDN上的随机头像
     */
    private static final String[] headImg = {
            "https://xd-video-pc-img.oss-cn-beijing.aliyuncs.com/xdclass_pro/default/head_img/12.jpeg",
            "https://xd-video-pc-img.oss-cn-beijing.aliyuncs.com/xdclass_pro/default/head_img/11.jpeg",
            "https://xd-video-pc-img.oss-cn-beijing.aliyuncs.com/xdclass_pro/default/head_img/13.jpeg",
            "https://xd-video-pc-img.oss-cn-beijing.aliyuncs.com/xdclass_pro/default/head_img/14.jpeg",
            "https://xd-video-pc-img.oss-cn-beijing.aliyuncs.com/xdclass_pro/default/head_img/15.jpeg"
    };


    @Override
    public int register(Map<String, String> userInfo) {
        User user = parseToUser(userInfo);

        if (user != null) {
            return userMapper.register(user);
        } else {
            return -1;
        }
    }

    @Override
    public User findUserByPhone(String phone) {
        return null;
    }

    private User parseToUser(Map<String, String> map) {
        if (map.containsKey("phone") && map.containsKey("pwd") && map.containsKey("name")) {
            User user = new User();
            user.setName(map.get("name"));
            user.setPwd(MD5Encrypt.encrypt(map.get("pwd")));
            user.setPhone(map.get("phone"));
            user.setHeadImg(getRandomImg());
            user.setCreateTime(new Date());

            return user;
        }

        return null;
    }

    private String getRandomImg() {
        int size = headImg.length;
        return headImg[new Random().nextInt(size)];
    }
}
