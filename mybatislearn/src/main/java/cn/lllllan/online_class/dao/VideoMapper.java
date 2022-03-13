package cn.lllllan.online_class.dao;

import cn.lllllan.online_class.domain.Video;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface VideoMapper {

    /**
     * 根据视频id查找视频对象
     *
     * @param videoId
     * @return
     */
    Video selectById(@Param("video_id") int videoId);

    /**
     * 使用 select 注解，或者到 VideoMapper.xml 中进行配置
     *
     * @return
     */
    @Select("select * from video")
    List<Video> selectList();

    int add(Video video);

    int addBatch(List<Video> list);
}
