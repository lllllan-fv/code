package cn.lllllan.online_class;

import cn.lllllan.online_class.dao.VideoMapper;
import cn.lllllan.online_class.domain.Video;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class SqlSessionDemo {

    public static void main(String[] args) throws IOException {

        String resource = "config/mybatis-config.xml";

        //读取配置文件
        InputStream inputStream = Resources.getResourceAsStream(resource);

        //构建Session工厂
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        //获取Session
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            VideoMapper videoMapper = sqlSession.getMapper(VideoMapper.class);
//
//            Video video = videoMapper.selectById(44);
//
//            System.out.println(video.toString());
//
//            List<Video> videoList = videoMapper.selectList();
//
//            System.out.println(videoList.toString());

            Video video1 = new Video();
            video1.setTitle("小滴课堂面试专题900道");
            video1.setCoverImg("xdclass.net/aaa.png");
            video1.setPoint(9.4);
            video1.setCreateTime(new Date());
            video1.setPrice(9900);
            video1.setSummary("这个是面试专题概要");

            Video video2 = new Video();
            video2.setTitle("小滴课堂面试专题900道");
            video2.setCoverImg("xdclass.net/aaa.png");
            video2.setPoint(9.4);
            video2.setCreateTime(new Date());
            video2.setPrice(9900);
            video2.setSummary("这个是面试专题概要");

//            int rows = videoMapper.add(video1);
//            System.out.println(rows);

            List<Video> list = new LinkedList<>();
            list.add(video1);
            list.add(video2);
            int i = videoMapper.addBatch(list);

            System.out.println(i);
        }

    }
}
