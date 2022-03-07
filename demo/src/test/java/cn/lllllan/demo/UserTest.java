package cn.lllllan.demo;

import cn.lllllan.demo.controller.UserController;
import cn.lllllan.demo.domain.User;
import cn.lllllan.demo.utils.JsonData;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplication.class})
public class UserTest {

    @Autowired
    private UserController userController;

    @Test
    public void loginTest() {
        User user = new User("TheShy", "123");
        JsonData jsonData = userController.userLogin(user);

        TestCase.assertEquals(0, jsonData.getCode());
    }
}
