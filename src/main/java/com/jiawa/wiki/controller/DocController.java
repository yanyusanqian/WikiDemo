package com.jiawa.wiki.controller;


import com.jiawa.wiki.req.DocQueryReq;
import com.jiawa.wiki.req.DocSaveReq;
import com.jiawa.wiki.resp.DocQueryResp;
import com.jiawa.wiki.resp.CommonResp;
import com.jiawa.wiki.resp.PageResp;
import com.jiawa.wiki.service.DocService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/doc")
public class DocController {
    
    @Resource
    private DocService docService;

    @GetMapping("/all/{ebookId}")
    public CommonResp all(@PathVariable Long ebookId){
        CommonResp<List<DocQueryResp>> response = new CommonResp<>();
        List<DocQueryResp> list = docService.all(ebookId);
        response.setContent(list);
        return response;
    }

    @GetMapping("/list")
    public CommonResp list(@Valid DocQueryReq docQueryReq){
        CommonResp<PageResp<DocQueryResp>> response = new CommonResp<>();
        PageResp<DocQueryResp> list = docService.list(docQueryReq);
        response.setContent(list);
        return response;
    }

    @PostMapping ("/save")
    public CommonResp save(@Valid @RequestBody DocSaveReq docSaveReq){
        CommonResp response = new CommonResp<>();
        docService.save(docSaveReq);
        return response;
    }

    @DeleteMapping("/delete/{idsStr}")
    public CommonResp delete(@PathVariable String idsStr){
        CommonResp response = new CommonResp<>();
        List<String> list = Arrays.asList(idsStr.split(","));
        docService.delete(list);
        return response;
    }

    @GetMapping("/find-content/{id}")
    public CommonResp findContent(@Valid Long id){
        CommonResp<String> response = new CommonResp<>();
        String content = docService.findContent(id);
        response.setContent(content);
        return response;
    }
}
