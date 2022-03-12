package cn.lllllan.demo.controller;

import cn.lllllan.demo.task.AsyncTask;
import cn.lllllan.demo.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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

    @RequestMapping("future")
    public JsonData TestFutureAsync() {
        long begin = System.currentTimeMillis();

        Future<String> stringFuture = asyncTask.task4();
        Future<String> stringFuture1 = asyncTask.task5();
        for (; ; ) {
            if (stringFuture.isDone()) {
                try {
                    System.out.println(stringFuture.get());
                    System.out.println(stringFuture1.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } finally {
                    break;
                }
            }
        }

        long end = System.currentTimeMillis();
        return JsonData.buildSuccess(end - begin);
    }
}
