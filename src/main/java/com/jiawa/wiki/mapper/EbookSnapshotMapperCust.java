package com.jiawa.wiki.mapper;


import com.jiawa.wiki.resp.StatisticResp;

import java.util.List;

public interface EbookSnapshotMapperCust {
    public void genSnapshot();

    public List<StatisticResp> getStatistic();

    public List<StatisticResp> get30Statistic();
}
