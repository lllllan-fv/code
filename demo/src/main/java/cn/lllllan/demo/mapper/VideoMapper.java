package cn.lllllan.demo.mapper;

import cn.lllllan.demo.domain.Video;

import java.util.HashMap;
import java.util.Map;

public class VideoMapper {

    private static Map<Integer, Video> videoMap = new HashMap<>();

    static {
        videoMap.put(1, new Video(1, "Java基础课程"));
        videoMap.put(1, new Video(2, "SpringBoot"));
        videoMap.put(1, new Video(3, "SpringMVC"));
        videoMap.put(1, new Video(4, "SpringCloud"));
    }
}
