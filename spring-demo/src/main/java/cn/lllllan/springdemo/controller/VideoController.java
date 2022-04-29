package cn.lllllan.springdemo.controller;

import cn.lllllan.springdemo.domain.Video;
import cn.lllllan.springdemo.service.VideoService;
import cn.lllllan.springdemo.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @RequestMapping("list")
    public Object listVideo() {
//        http://localhost:8081/video/list
        List<Video> videos = videoService.listVideo();
        return JsonData.success(videos);
    }
}
