package cn.lllllan.demo.controller;

import cn.lllllan.demo.domain.Video;
import cn.lllllan.demo.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @GetMapping("listVideo")
    public List<Video> listVideo() {
        return videoService.listVideo();
    }
}
