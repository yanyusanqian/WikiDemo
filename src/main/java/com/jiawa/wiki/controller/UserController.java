package com.jiawa.wiki.controller;


import com.alibaba.fastjson.JSON;
import com.jiawa.wiki.req.UserLoginReq;
import com.jiawa.wiki.req.UserQueryReq;
import com.jiawa.wiki.req.UserResetPasswordReq;
import com.jiawa.wiki.req.UserSaveReq;
import com.jiawa.wiki.resp.CommonResp;
import com.jiawa.wiki.resp.UserLoginResp;
import com.jiawa.wiki.resp.UserQueryResp;
import com.jiawa.wiki.resp.PageResp;
import com.jiawa.wiki.service.UserService;
import com.jiawa.wiki.utils.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    @Resource
    private UserService userService;

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private RedisTemplate redisTemplate;


    @GetMapping("/list")
    public CommonResp list(@Valid UserQueryReq userQueryReq){
        CommonResp<PageResp<UserQueryResp>> response = new CommonResp<>();
        PageResp<UserQueryResp> list = userService.list(userQueryReq);
        response.setContent(list);
        return response;
    }

    @PostMapping ("/save")
    public CommonResp save(@Valid @RequestBody UserSaveReq userSaveReq){
        userSaveReq.setPassword(DigestUtils.md5DigestAsHex(userSaveReq.getPassword().getBytes()));
        CommonResp response = new CommonResp<>();
        userService.save(userSaveReq);
        return response;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id){
        CommonResp response = new CommonResp<>();
        userService.delete(id);
        return response;
    }

    @PostMapping ("/reset-password")
    public CommonResp resetPassword(@Valid @RequestBody UserResetPasswordReq req){
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp response = new CommonResp<>();
        userService.resetPassword(req);
        return response;
    }

    @PostMapping ("/login")
    public CommonResp login(@Valid @RequestBody UserLoginReq req){
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp<UserLoginResp> response = new CommonResp<>();
        UserLoginResp userLoginResp = userService.login(req);

        Long token = snowFlake.nextId();
        LOG.info("生成单点登录token:{}，并放入redis中", token.toString());
        userLoginResp.setToken(token.toString());
        redisTemplate.opsForValue().set(token.toString(), JSON.toJSONString(userLoginResp),3600*24, TimeUnit.SECONDS);
        response.setContent(userLoginResp);
        return response;
    }
}
