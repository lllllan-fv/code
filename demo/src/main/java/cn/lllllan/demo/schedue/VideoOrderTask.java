package cn.lllllan.demo.schedue;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * 定时统计订单
 */
@Component
public class VideoOrderTask {

    @Scheduled(fixedRate = 2000)
    public void sum() {
        System.out.println("当前交易总额 = " + new Random().nextInt(10000));
    }

}
