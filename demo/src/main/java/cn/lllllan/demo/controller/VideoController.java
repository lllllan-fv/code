package cn.lllllan.demo.controller;

import cn.lllllan.demo.domain.Video;
import cn.lllllan.demo.service.VideoService;
import cn.lllllan.demo.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pub/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @GetMapping("list")
    public Object listVideo() {
        return JsonData.buildSuccess(videoService.listVideo());
    }

    @PostMapping("save_chapter")
    public Object saveChapter(@RequestBody Video video) {
        System.out.println(video.getChapterList());
        return JsonData.buildSuccess(video);
    }
}
