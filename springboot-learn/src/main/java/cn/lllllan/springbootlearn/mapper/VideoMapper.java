package cn.lllllan.springbootlearn.mapper;

import cn.lllllan.springbootlearn.domain.Video;

import java.util.List;

public interface VideoMapper {

    /**
     * 查询视频列表
     *
     * @return
     */
    List<Video> listVideo();
}
