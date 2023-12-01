package com.jiawa.wiki.controller;

import com.jiawa.wiki.resp.CommonResp;
import com.jiawa.wiki.resp.StatisticResp;
import com.jiawa.wiki.service.EbookSnapshotService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/ebook-snapshot")
public class EbookSnapshotController {

    @Resource
    private EbookSnapshotService snapshotService;

    @GetMapping("/get-statistic")
    public CommonResp getStatistic(){
        List<StatisticResp> statisticResps = snapshotService.getStatistic();
        CommonResp<List<StatisticResp>> resp = new CommonResp<>();
        resp.setContent(statisticResps);
        return resp;
    }

    @GetMapping("/get-30-statistic")
    public CommonResp get30Statistic() {
        List<StatisticResp> statisticResp = snapshotService.get30Statistic();
        CommonResp<List<StatisticResp>> commonResp = new CommonResp<>();
        commonResp.setContent(statisticResp);
        return commonResp;
    }
}
