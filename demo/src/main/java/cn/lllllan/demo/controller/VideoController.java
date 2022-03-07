package cn.lllllan.demo.controller;

import cn.lllllan.demo.service.VideoService;
import cn.lllllan.demo.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @GetMapping("listVideo")
    public Object listVideo() {
        return JsonData.buildSuccess(videoService.listVideo());
    }
}
