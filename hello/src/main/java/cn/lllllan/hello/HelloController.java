package cn.lllllan.hello;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class HelloController {

    @RequestMapping("/")
    public Object listVideo() {
        return "hello spring boot";
    }
}