package cn.lllllan.springredis;

import cn.lllllan.springredis.domain.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class SpringRedisApplicationTests {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void contextLoads() throws JsonProcessingException {
        User user = new User("lllllan", 22);
        String s = new ObjectMapper().writeValueAsString(user);

        redisTemplate.opsForValue().set("lllllan", user);
        System.out.println(redisTemplate.opsForValue().get("lllllan"));

    }

}
