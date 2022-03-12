package cn.lllllan.demo.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * 定时统计订单
 */
@Component
public class VideoOrderTask {

    /**
     * 每两秒执行一次
     */
    @Scheduled(fixedRate = 2000)
    public void fixedRateSchedule() {
        System.out.println("（每两秒定时任务）当前交易总额 = " + new Random().nextInt(10000));
    }

    /**
     * cron表达式
     */
    @Scheduled(cron = "*/1 * * * * *")
    public void cronSchedule() {
        System.out.println("cron定时任务");
    }

    /**
     * 完成后开始计时
     */
    @Scheduled(fixedDelay = 2000)
    public void fixedDelaySchedule() {
        System.out.println("delay定时任务");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
