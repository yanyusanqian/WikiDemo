package com.jiawa.wiki.controller;


import com.jiawa.wiki.domain.Test;
import com.jiawa.wiki.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/test")
public class TestController {
    @Resource
    private TestService testService;

    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);

    @Resource
    private RedisTemplate redisTemplate;

    @RequestMapping("/hello")
    public String hello(){
        return "Hello world";
    }

    @RequestMapping("/hello/post")
    public String helloPost(String name){
        return "Hello world! Post, " + name;
    }

    @GetMapping("/list")
    public List<Test> list(){
        return testService.list();
    }

    /**
     * 向redis内添加数据
     * @param key
     * @param value
     * @return
     */
    @RequestMapping("/redis/set/{key}/{value}")
    public String set(@PathVariable Long key, @PathVariable String value) {
        redisTemplate.opsForValue().set(key, value, 3600, TimeUnit.SECONDS);
        LOG.info("key: {}, value: {}", key, value);
        return "success";
    }

    /**
     * 查询redis中数据
     * @param key
     * @return
     */
    @RequestMapping("/redis/get/{key}")
    public Object get(@PathVariable String key) {
        Object object = redisTemplate.opsForValue().get(key);
        LOG.info("key: {}, value: {}", key, object);
        return object;
    }
}
