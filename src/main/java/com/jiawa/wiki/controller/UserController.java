package com.jiawa.wiki.controller;


import com.jiawa.wiki.req.UserQueryReq;
import com.jiawa.wiki.req.UserResetPasswordReq;
import com.jiawa.wiki.req.UserSaveReq;
import com.jiawa.wiki.resp.CommonResp;
import com.jiawa.wiki.resp.UserQueryResp;
import com.jiawa.wiki.resp.PageResp;
import com.jiawa.wiki.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    @Resource
    private UserService userService;


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
}
