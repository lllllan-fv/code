package cn.lllllan.demo.controller;

import cn.lllllan.demo.task.AsyncTask;
import cn.lllllan.demo.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pub/async")
public class AsyncController {

    @Autowired
    private AsyncTask asyncTask;

    @RequestMapping("async")
    public JsonData testAsync() {
        long begin = System.currentTimeMillis();
        asyncTask.task1();
        asyncTask.task2();
        asyncTask.task3();
        long end = System.currentTimeMillis();
        
        return JsonData.buildSuccess(end - begin);
    }
}
