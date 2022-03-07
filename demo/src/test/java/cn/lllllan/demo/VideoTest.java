package cn.lllllan.demo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplication.class})
public class VideoTest {

    @Before
    public void testBefore() {
        System.out.println("Before");
    }

    @Test
    public void testTest1() {
        System.out.println("Test1");
    }

    @Test
    public void testTest2() {
        System.out.println("Test2");
    }

    @After
    public void testAfter() {
        System.out.println("After");
    }
}
