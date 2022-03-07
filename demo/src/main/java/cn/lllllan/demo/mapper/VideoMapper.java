package cn.lllllan.demo.mapper;

import cn.lllllan.demo.domain.Video;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class VideoMapper {

    private static Map<Integer, Video> videoMap = new HashMap<>();

    static {
        videoMap.put(1, new Video(1, "Java基础课程"));
        videoMap.put(2, new Video(2, "SpringBoot"));
        videoMap.put(3, new Video(3, "SpringMVC"));
        videoMap.put(4, new Video(4, "SpringCloud"));
    }

    public List<Video> listVideo() {
        return new ArrayList<>(videoMap.values());
    }
}
