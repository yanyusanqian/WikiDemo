package com.jiawa.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.wiki.domain.Content;
import com.jiawa.wiki.domain.Doc;
import com.jiawa.wiki.domain.DocExample;
import com.jiawa.wiki.mapper.ContentMapper;
import com.jiawa.wiki.mapper.DocMapper;
import com.jiawa.wiki.req.DocQueryReq;
import com.jiawa.wiki.req.DocSaveReq;
import com.jiawa.wiki.resp.DocQueryResp;
import com.jiawa.wiki.resp.PageResp;
import com.jiawa.wiki.utils.CopyUtils;
import com.jiawa.wiki.utils.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DocService {
    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

    @Resource
    private DocMapper docMapper;

    @Resource
    private ContentMapper contentMapper;

    @Resource
    private SnowFlake snowFlake;

    public List<DocQueryResp> all(Long ebookId){
        DocExample docExample = new DocExample();
        // 一定按条件查询，防止前端通过链接查询出所有数据
        docExample.createCriteria().andEbookIdEqualTo(ebookId);
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);
        LOG.info("EEEEEEEEDoclist:{}",docList);

        List<DocQueryResp> respList = CopyUtils.copyList(docList, DocQueryResp.class);


        return respList;
    }

    public PageResp<DocQueryResp> list(DocQueryReq docQueryReq){
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        DocExample.Criteria criteria = docExample.createCriteria();
        PageHelper.startPage(docQueryReq.getPage(), docQueryReq.getSize());
        List<Doc> docList = docMapper.selectByExample(docExample);
        LOG.info("EEEEEEEEDoclist:{}",docList);

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        // 总行数
        LOG.info("总行数:{}",pageInfo.getTotal());
        // 总页数
        LOG.info("总行数:{}",pageInfo.getPages());

        // List<DocResp> respList = new ArrayList<>();
        // for (Doc doc : docList) {
        //     // DocResp docResp = new DocResp();
        //     // BeanUtils.copyProperties(doc, docResp);
        //
        //     DocResp docResp = CopyUtils.copy(doc, DocResp.class);
        //     respList.add(docResp);
        // }

        List<DocQueryResp> respList = CopyUtils.copyList(docList, DocQueryResp.class);

        PageResp<DocQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);

        LOG.info("PPPPPageResp:{}",pageResp);

        return pageResp;
    }

    /**
     * 保存，由id判断是更新还是新增
     */
    public void save(DocSaveReq docSaveReq){
        LOG.info("DocSaveReq.toString():{}" ,docSaveReq.toString());
        Doc doc = CopyUtils.copy(docSaveReq, Doc.class);
        // 只复制DocSaveReq里的content
        Content content = CopyUtils.copy(docSaveReq, Content.class);
        if(ObjectUtils.isEmpty(docSaveReq.getId())){
            LOG.info("电子书id:{}",doc.getEbookId());
            // 新增
            doc.setId(snowFlake.nextId());
            docMapper.insert(doc);
            content.setId(doc.getId());
            contentMapper.insert(content);
        }else{
            // 更新
            docMapper.updateByPrimaryKey(doc);
            // 带大字段更新
            int count = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            if(count == 0){
                contentMapper.insert(content);
            }
        }
    }

    public void delete(Long id){
        docMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据id列表删除一串
     * @param ids
     */
    public void delete(List<String> ids){
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);
    }

    public String findContent(Long id){
       Content content = contentMapper.selectByPrimaryKey(id);
       return content.getContent();
    }

}
