package cn.lllllan.springbootlearn.controller;

import cn.lllllan.springbootlearn.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @RequestMapping("list")
    public Object listVideo() {
        return videoService.listVideo();
    }
}
