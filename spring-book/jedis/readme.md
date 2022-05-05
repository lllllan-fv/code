

## 添加依赖

```xml
<dependencies>
    <dependency>
        <groupId>redis.clients</groupId>
        <artifactId>jedis</artifactId>
        <version>3.2.0</version>
    </dependency>

    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>fastjson</artifactId>
        <version>1.2.62</version>
    </dependency>
</dependencies>
```



## 服务器设置



### 开启安全组

到云服务器管理开放 `6379` 端口号



### 配置文件设置



1. 找到所有 `bind *` 的语句全部注释掉
2. 关闭保护模式，设置参数 `protected-mode` 为 no
3. 设置 redis 密码，`requirepass [password]`



### 重启服务器

```bash
redis-server my_config/redis.config # 配置文件
```



## 连接 redis

```java
import redis.clients.jedis.Jedis;

public class PingTest {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("lllllan.cn", 6379);

        jedis.auth("[password]");

        System.out.println(jedis.ping());
    }
}
```



## 事务

```java
Transaction multi = jedis.multi();

try {
    multi.set("name", "lllllan");
    multi.set("age", "22");
    List<Object> exec = multi.exec();

    for (Object object : exec) {
        System.out.println(object.toString());
    }
} catch (Exception e) {
    multi.discard();
    e.printStackTrace();
}
```
