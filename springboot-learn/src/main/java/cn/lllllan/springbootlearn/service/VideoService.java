package cn.lllllan.springbootlearn.service;

import cn.lllllan.springbootlearn.domain.Video;
import cn.lllllan.springbootlearn.domain.VideoBanner;

import java.util.List;

public interface VideoService {

    List<Video> listVideo();

    List<VideoBanner> listBanner();

    Video findDetailById(int videoId);
}
