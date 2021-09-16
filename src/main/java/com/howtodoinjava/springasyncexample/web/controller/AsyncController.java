package com.howtodoinjava.springasyncexample.web.controller;

import com.howtodoinjava.springasyncexample.web.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;

import java.util.Collections;
import java.util.Map;

@RestController
public class AsyncController {

    @Autowired
    private AsyncService asyncService;

    @GetMapping(path="/api/v1/task")
    public WebAsyncTask<Map<String, Object>> simpleAsyncTask(@RequestParam(defaultValue="5") long t) {
        return new WebAsyncTask<Map<String, Object>>(10000, () -> {
            Thread.sleep(t * 1000);
            return Collections.singletonMap("key", "success");
        });
    }

    @GetMapping("/hello")
    public WebAsyncTask sayHello(@RequestParam(defaultValue="Demo user") String name){
        System.out.println("service start...");
        WebAsyncTask task = new WebAsyncTask(4000, () -> {
            System.out.println("task execution start...");
            int waitSeconds = 2;
            if("timeout".equals(name)) {
                waitSeconds = 5;
            }
            Thread.sleep(waitSeconds * 1000);
            if("error".equals(name)) {
                throw new RuntimeException("Manual exception at runtime");
            }
            System.out.println("task execution end...");
            return "Welcome "+name+"!";
        });
        task.onTimeout(()->{
            System.out.println("onTimeout...");
            return "Request timed out...";
        });
        task.onError(()->{
            System.out.println("onError...");
            return "Some error occurred...";
        });
        task.onCompletion(()->{
            System.out.println("onCompletion...");
        });
        System.out.println("service end...");
        return task;
    }

    @GetMapping("/asyncTest")
    public void asyncControllerMethod(@RequestParam String user) {
        asyncService.someAsyncService(user);
    }
}
