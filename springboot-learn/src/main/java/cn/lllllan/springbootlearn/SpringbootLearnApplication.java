package cn.lllllan.springbootlearn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.lllllan.springbootlearn.mapper")
public class SpringbootLearnApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootLearnApplication.class, args);
    }

}
