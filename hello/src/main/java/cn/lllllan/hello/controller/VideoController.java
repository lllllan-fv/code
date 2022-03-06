package cn.lllllan.hello.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/video")
public class VideoController {

    @RequestMapping("list")
    public Object list() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "面试专题");
        map.put("2", "SpringCloud");
        return map;
    }

    @PostMapping("postList")
    public Object postList() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "面试专题");
        map.put("2", "SpringCloud");
        return map;
    }

    @GetMapping("getList")
    public Object getList() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "面试专题");
        map.put("2", "SpringCloud");
        return map;
    }
}
