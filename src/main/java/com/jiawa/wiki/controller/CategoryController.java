package com.jiawa.wiki.controller;


import com.jiawa.wiki.req.CategoryQueryReq;
import com.jiawa.wiki.req.CategorySaveReq;
import com.jiawa.wiki.resp.CommonResp;
import com.jiawa.wiki.resp.CategoryQueryResp;
import com.jiawa.wiki.resp.PageResp;
import com.jiawa.wiki.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/categor")
public class CategoryController {
    
    @Resource
    private CategoryService categorService;


    @GetMapping("/list")
    public CommonResp list(@Valid CategoryQueryReq categorQueryReq){
        CommonResp<PageResp<CategoryQueryResp>> response = new CommonResp<>();
        PageResp<CategoryQueryResp> list = categorService.list(categorQueryReq);
        response.setContent(list);
        return response;
    }

    @PostMapping ("/save")
    public CommonResp save(@Valid @RequestBody CategorySaveReq categorSaveReq){
        CommonResp response = new CommonResp<>();
        categorService.save(categorSaveReq);
        return response;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id){
        CommonResp response = new CommonResp<>();
        categorService.delete(id);
        return response;
    }
}
