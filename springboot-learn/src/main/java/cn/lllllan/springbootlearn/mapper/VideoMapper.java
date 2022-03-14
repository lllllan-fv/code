package cn.lllllan.springbootlearn.mapper;

import cn.lllllan.springbootlearn.domain.Video;
import cn.lllllan.springbootlearn.domain.VideoBanner;

import java.util.List;

public interface VideoMapper {

    /**
     * 查询视频列表
     *
     * @return
     */
    List<Video> listVideo();

    List<VideoBanner> listBanner();
}
