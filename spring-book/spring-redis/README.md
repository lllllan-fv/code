

## 依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```



## 配置

位置：`src/main/resources/application.properties`

```xml
# redis
spring.redis.host=lllllan.cn
spring.redis.port=6379
spring.redis.password=password
```



## 使用

```java
@SpringBootTest
class SpringRedisApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
        redisTemplate.opsForValue().set("name", "lllllan");
        System.out.println(redisTemplate.opsForValue().get("name"));
    }

}
```