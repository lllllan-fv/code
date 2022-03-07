package cn.lllllan.demo;

import cn.lllllan.demo.service.VideoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.Charset;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplication.class})
public class VideoTest {

    @Autowired
    private VideoService videoService;

    //  单元测试，对http请求进行模拟
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testVideoListApi() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/pub/video/list"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        int status = mvcResult.getResponse().getStatus();
        System.out.println(status);

        String contentAsString = mvcResult.getResponse().
                getContentAsString(Charset.defaultCharset()); // 防止中文乱码
        System.out.println(contentAsString);
    }

}
