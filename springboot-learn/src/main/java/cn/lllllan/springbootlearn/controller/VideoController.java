package cn.lllllan.springbootlearn.controller;

import cn.lllllan.springbootlearn.domain.Video;
import cn.lllllan.springbootlearn.domain.VideoBanner;
import cn.lllllan.springbootlearn.service.VideoService;
import cn.lllllan.springbootlearn.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pub/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    /**
     * 返回视频列表
     *
     * @return
     */
    @RequestMapping("list")
    public Object listVideo() {
        List<Video> videos = videoService.listVideo();
        return JsonData.buildSuccess(videos, "启动热部署");
    }

    /**
     * 返回轮播图列表
     *
     * @return
     */
    @RequestMapping("list_banner")
    public Object listBanner() {
        List<VideoBanner> videoBanners = videoService.listBanner();

        int i = 1 / 0;

        return JsonData.buildSuccess(videoBanners);
    }

    @RequestMapping("find_detail_by_id")
    public Object findDetailById(@RequestParam(value = "video_id", required = true) int videoId) {
        Video detailById = videoService.findDetailById(videoId);
        return JsonData.buildSuccess(detailById);
    }
}
