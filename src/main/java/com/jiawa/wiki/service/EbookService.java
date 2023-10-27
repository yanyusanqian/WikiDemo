package com.jiawa.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.wiki.domain.Ebook;
import com.jiawa.wiki.domain.EbookExample;
import com.jiawa.wiki.mapper.EbookMapper;
import com.jiawa.wiki.req.EbookQueryReq;
import com.jiawa.wiki.req.EbookSaveReq;
import com.jiawa.wiki.resp.EbookQueryResp;
import com.jiawa.wiki.resp.PageResp;
import com.jiawa.wiki.utils.CopyUtils;
import com.jiawa.wiki.utils.SnowFlake;
import com.sun.org.apache.bcel.internal.generic.ACONST_NULL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {
    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    @Resource
    private EbookMapper ebookMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResp<EbookQueryResp> list(EbookQueryReq ebookQueryReq){
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if(!ObjectUtils.isEmpty(ebookQueryReq.getName())) {
            criteria.andNameLike("%" + ebookQueryReq.getName() + "%");
        }
        if(!ObjectUtils.isEmpty(ebookQueryReq.getCategory2Id())) {
            criteria.andCategory2IdEqualTo(ebookQueryReq.getCategory2Id());
        }
        PageHelper.startPage(ebookQueryReq.getPage(), ebookQueryReq.getSize());
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
        LOG.info("EEEEEEEEEbooklist:{}",ebookList);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        // 总行数
        LOG.info("总行数:{}",pageInfo.getTotal());
        // 总页数
        LOG.info("总行数:{}",pageInfo.getPages());

        // List<EbookResp> respList = new ArrayList<>();
        // for (Ebook ebook : ebookList) {
        //     // EbookResp ebookResp = new EbookResp();
        //     // BeanUtils.copyProperties(ebook, ebookResp);
        //
        //     EbookResp ebookResp = CopyUtils.copy(ebook, EbookResp.class);
        //     respList.add(ebookResp);
        // }

        List<EbookQueryResp> respList = CopyUtils.copyList(ebookList, EbookQueryResp.class);

        PageResp<EbookQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);

        LOG.info("PPPPPageResp:{}",pageResp);

        return pageResp;
    }

    /**
     * 保存，由id判断是更新还是新增
     */
    public void save(EbookSaveReq ebookSaveReq){
        LOG.info("EbookSaveReq.toString():{}" ,ebookSaveReq.toString());
        Ebook ebook = CopyUtils.copy(ebookSaveReq, Ebook.class);
        if(ObjectUtils.isEmpty(ebookSaveReq.getId())){
            // 新增
            ebook.setId(snowFlake.nextId());
            ebookMapper.insert(ebook);
        }else{
            // 更新
            ebookMapper.updateByPrimaryKey(ebook);
        }
    }

    public void delete(Long id){
        ebookMapper.deleteByPrimaryKey(id);
    }
}
