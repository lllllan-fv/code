package cn.lllllan.springbootlearn.service.impl;

import cn.lllllan.springbootlearn.domain.Video;
import cn.lllllan.springbootlearn.mapper.VideoMapper;
import cn.lllllan.springbootlearn.service.VideoService;
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
