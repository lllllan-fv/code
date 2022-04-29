## 基本依赖

位置：`pom.xml`

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
        <exclusions>
            <exclusion>
                <groupId>org.junit.vintage</groupId>
                <artifactId>junit-vintage-engine</artifactId>
            </exclusion>
        </exclusions>
    </dependency>

    <!--mybatis-->
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>2.2.2</version>
    </dependency>

    <!--数据库连接，记得runtime注释掉-->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <!--<scope>runtime</scope>-->
    </dependency>

    <!--通用包，不知道干嘛-->
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-lang3</artifactId>
        <version>3.9</version>
    </dependency>

    <!--解决jwt跨域问题-->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt</artifactId>
        <version>0.7.0</version>
    </dependency>

    <!--谷歌提供的缓存-->
    <dependency>
        <groupId>com.google.guava</groupId>
        <artifactId>guava</artifactId>
        <version>19.0</version>
    </dependency>

</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <fork>true</fork><!--必须添加这个配置-->
            </configuration>
        </plugin>
    </plugins>
</build>
```



## MyBatis 配置



### 数据库配置

位置：`resources\application.properties`

```properties
server.port=8081
#==============================数据库相关配置========================================
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://lllllan.cn:3306/xdclass?useUnicode=true&characterEncoding=utf-8&useSSL=false
spring.datasource.username=root
spring.datasource.password=Lllllan@0424
#使用阿里巴巴druid数据源，默认使用自带的
#spring.datasource.type =com.alibaba.druid.pool.DruidDataSource
#开启控制台打印sql
mybatis.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
# mybatis 下划线转驼峰配置,两者都可以
#mybatis.configuration.mapUnderscoreToCamelCase=true
mybatis.configuration.map-underscore-to-camel-case=true
#配置扫描
mybatis.mapper-locations=classpath:mapper/*.xml
#配置xml的结果别名
mybatis.type-aliases-package=cn.lllllan.springdemo.domain
```



### 配置扫描

位置：`java\cn\lllllan\springdemo\SpringDemoApplication.java`

```java {2}
@SpringBootApplication
@MapperScan("cn.llllan.springdemo.mapper")
public class SpringDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDemoApplication.class, args);
    }

}
```



### 创建相关 Mapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="cn.lllllan.springdemo.mapper.DemoMapper">

</mapper>
```



## 接口



### 实体类

domain 层的实体类，具备数据库中所有的同名字段。



### 数据库操作

位置：`resources\mapper\VideoMapper.xml`

- namespace 和具体的 Mapper 对应
- id 即同名的方法
- resultType 将查询数据存入对应的实体类中

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="cn.lllllan.springdemo.mapper.VideoMapper">

    <select id="listVideo" resultType="Video">
        select * from video
    </select>

</mapper>
```



### Mapper

连接 mybatis 操作数据的类

```java
package cn.lllllan.springdemo.mapper;

import cn.lllllan.springdemo.domain.Video;

import java.util.List;

public interface VideoMapper {

    List<Video> listVideo();
}
```



### Service

```java
package cn.lllllan.springdemo.service;

import cn.lllllan.springdemo.domain.Video;

import java.util.List;

public interface VideoService {

    List<Video> listVideo();
}
```



- 注意注解，一定要有 @Service
- 继承相应 Service
- 使用 Mapper 的方法

```java
@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoMapper videoMapper;

    @Override
    public List<Video> listVideo() {
        return videoMapper.listVideo();
    }
}
```



### Controller

注意所有的注解

- 调用线相关Service 的方法

```java
@RestController
@RequestMapping("video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @RequestMapping("list")
    public Object listVideo() {
//        http://localhost:8081/video/list
        return videoService.listVideo();
    }
}
```



## JsonData

```java
package cn.lllllan.springdemo.utils;

public class JsonData {

    private int code;

    private Object data;

    private String msg;

    public JsonData() {
    }

    public JsonData(int code, Object data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public static JsonData success(Object data, String msg) {
        return new JsonData(0, data, msg);
    }

    public static JsonData success(Object data) {
        return new JsonData(0, data, null);
    }

    public static JsonData success() {
        return new JsonData(0, null, null);
    }

    public static JsonData error(int code, String msg) {
        return new JsonData(code, null, msg);
    }

    public static JsonData error(String msg) {
        return new JsonData(-1, null, msg);
    }

    public static JsonData error() {
        return new JsonData(-1, null, null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
```



## 热部署

[小滴课堂-视频播放 (16web.net)](https://16web.net/?access_token=xdclasseyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ4ZGNsYXNzIiwicm9sZXMiOiIxIiwiaW1nIjoiaHR0cHM6Ly90aGlyZHd4LnFsb2dvLmNuL21tb3Blbi92aV8zMi9nSkRraWFvMXBVWkduQUVjWUtTVXdRU2NsSmliTWFNYVR1cER2aWFSZnE4dFJjRk5aTWtDNUNCYnQ5QjZJSEtkZ2FqRWNhRGlidXJFQU83anp4OHZXbDFid2cvMTMyIiwiaWQiOjY4MDM3NjEsIm5hbWUiOiLng63ooYDlsJHlubQiLCJpYXQiOjE2NTEyMDE4MzYsImV4cCI6MTY1MTgwNjYzNn0.zz-eO6Sp9IiDAUzoFAJob3h7v6PY6wEHQU2nyVdsEKU&head_img=https://thirdwx.qlogo.cn/mmopen/vi_32/gJDkiao1pUZGnAEcYKSUwQSclJibMaMaTupDviaRfq8tRcFNZMkC5CBbt9B6IHKdgajEcaDiburEAO7jzx8vWl1bwg/132&name=热血少年#/dplayer?video_id=52&e_id=102118)

```xml
<!--热部署-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <optional>true</optional>
</dependency>
```



## 自定义异常



### 自定义异常

```java
package cn.lllllan.springdemo.exception;

public class LanException extends RuntimeException {

    private Integer code;

    private String msg;

    public LanException() {
    }

    public LanException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
```



### 异常处理

- 注意注解

```java
import cn.lllllan.springdemo.utils.JsonData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class VideoExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(VideoExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonData handle(Exception e) {

        logger.error("[ 系统异常 ]{}", e);

        if (e instanceof LanException) {
            LanException lanException = (LanException) e;
            return JsonData.error(lanException.getCode(), lanException.getMsg());
        } else {
            return JsonData.error("全局异常，未知错误");
        }
    }
}
```