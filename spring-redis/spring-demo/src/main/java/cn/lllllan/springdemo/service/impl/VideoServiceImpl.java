package cn.lllllan.springdemo.service.impl;

import cn.lllllan.springdemo.domain.Video;
import cn.lllllan.springdemo.mapper.VideoMapper;
import cn.lllllan.springdemo.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoMapper videoMapper;

    @Override
    public List<Video> listVideo() {
        return videoMapper.listVideo();
    }
}
