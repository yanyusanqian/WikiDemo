package com.jiawa.wiki.controller;


import com.jiawa.wiki.domain.Test;
import com.jiawa.wiki.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
public class TestController {


    @Resource
    private TestService testService;

    @RequestMapping("/hello")
    public String hello(){
        return "Hello world";
    }

    @RequestMapping("/hello/post")
    public String helloPost(String name){
        return "Hello world! Post, " + name;
    }

    @GetMapping("/test/list")
    public List<Test> list(){
        return testService.list();
    }
}
