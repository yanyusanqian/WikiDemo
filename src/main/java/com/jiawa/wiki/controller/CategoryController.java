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
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    
    @Resource
    private CategoryService categoryService;

    @GetMapping("/all")
    public CommonResp all(){
        CommonResp<List<CategoryQueryResp>> response = new CommonResp<>();
        List<CategoryQueryResp> list = categoryService.all();
        response.setContent(list);
        return response;
    }

    @GetMapping("/list")
    public CommonResp list(@Valid CategoryQueryReq categoryQueryReq){
        CommonResp<PageResp<CategoryQueryResp>> response = new CommonResp<>();
        PageResp<CategoryQueryResp> list = categoryService.list(categoryQueryReq);
        response.setContent(list);
        return response;
    }

    @PostMapping ("/save")
    public CommonResp save(@Valid @RequestBody CategorySaveReq categorySaveReq){
        CommonResp response = new CommonResp<>();
        categoryService.save(categorySaveReq);
        return response;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id){
        CommonResp response = new CommonResp<>();
        categoryService.delete(id);
        return response;
    }
}
