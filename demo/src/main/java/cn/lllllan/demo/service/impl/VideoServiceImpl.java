package cn.lllllan.demo.service.impl;

import cn.lllllan.demo.domain.Video;
import cn.lllllan.demo.mapper.VideoMapper;
import cn.lllllan.demo.service.VideoService;
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
