package com.jiawa.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.wiki.domain.Content;
import com.jiawa.wiki.domain.Doc;
import com.jiawa.wiki.domain.DocExample;
import com.jiawa.wiki.exception.BusinessException;
import com.jiawa.wiki.exception.BusinessExceptionCode;
import com.jiawa.wiki.mapper.ContentMapper;
import com.jiawa.wiki.mapper.DocMapper;
import com.jiawa.wiki.mapper.DocMapperCust;
import com.jiawa.wiki.req.DocQueryReq;
import com.jiawa.wiki.req.DocSaveReq;
import com.jiawa.wiki.resp.DocQueryResp;
import com.jiawa.wiki.resp.PageResp;
import com.jiawa.wiki.utils.CopyUtils;
import com.jiawa.wiki.utils.RedisUtil;
import com.jiawa.wiki.utils.RequestContext;
import com.jiawa.wiki.utils.SnowFlake;
import com.jiawa.wiki.websocket.WebSocketServer;
//import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DocService {
    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

    @Resource
    private DocMapper docMapper;

    @Resource
    private DocMapperCust docMapperCust;

    @Resource
    private ContentMapper contentMapper;

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private WsService wsService;


    //@Resource
    //private RocketMQTemplate rocketMQTemplate;


    public List<DocQueryResp> all(Long ebookId) {
        DocExample docExample = new DocExample();
        // 一定按条件查询，防止前端通过链接查询出所有数据
        docExample.createCriteria().andEbookIdEqualTo(ebookId);
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);
        LOG.info("EEEEEEEEDoclist:{}", docList);

        List<DocQueryResp> respList = CopyUtils.copyList(docList, DocQueryResp.class);


        return respList;
    }

    public PageResp<DocQueryResp> list(DocQueryReq docQueryReq) {
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        DocExample.Criteria criteria = docExample.createCriteria();
        PageHelper.startPage(docQueryReq.getPage(), docQueryReq.getSize());
        List<Doc> docList = docMapper.selectByExample(docExample);
        LOG.info("EEEEEEEEDoclist:{}", docList);

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        // 总行数
        LOG.info("总行数:{}", pageInfo.getTotal());
        // 总页数
        LOG.info("总行数:{}", pageInfo.getPages());

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

        LOG.info("PPPPPageResp:{}", pageResp);

        return pageResp;
    }

    /**
     * 保存，由id判断是更新还是新增
     * 新增时需要同时像两张表插入信息，所以要加上事务
     * 防止向一张表插入成功，另一张表没成功，导致数据信息错误的情况
     * springboot中添加事务就是在方法前加上Transactional注解
     * 该注解与Async一样，方法本身与调用方法的方法不能在同一个类里，否则就不生效
     */
    @Transactional
    public void save(DocSaveReq docSaveReq) {
        LOG.info("DocSaveReq.toString():{}", docSaveReq.toString());
        Doc doc = CopyUtils.copy(docSaveReq, Doc.class);
        // 只复制DocSaveReq里的content
        Content content = CopyUtils.copy(docSaveReq, Content.class);
        if (ObjectUtils.isEmpty(docSaveReq.getId())) {
            LOG.info("电子书id:{}", doc.getEbookId());
            // 新增
            doc.setId(snowFlake.nextId());
            doc.setViewCount(0);
            doc.setVoteCount(0);
            docMapper.insert(doc);
            content.setId(doc.getId());
            contentMapper.insert(content);
        } else {
            // 更新
            docMapper.updateByPrimaryKey(doc);
            // 带大字段更新
            int count = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            if (count == 0) {
                contentMapper.insert(content);
            }
        }
    }

    public void delete(Long id) {
        docMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据id列表删除一串
     *
     * @param ids
     */
    public void delete(List<String> ids) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);
    }

    public String findContent(Long id) {
        Content content = contentMapper.selectByPrimaryKey(id);
        // 文档阅读数+1
        docMapperCust.increaseViewCount(id);
        if (ObjectUtils.isEmpty(content))
            return "";
        else
            return content.getContent();
    }

    /**
     * 点赞
     */
    public void vote(Long id) {
         //docMapperCust.increaseVoteCount(id);
        // 远程IP+doc.id作为key，24小时内不能重复
        String ip = RequestContext.getRemoteAddr();
        if (redisUtil.validateRepeat("DOC_VOTE_" + id + "_" + ip, 5000)) {
            docMapperCust.increaseVoteCount(id);
        } else {
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }
        // 推送消息
        Doc doc = docMapper.selectByPrimaryKey(id);
        // 获得流水号，保证不同线程内的同一流程中的流水号一致，方便运维
        String logId = MDC.get("LOG_ID");
        wsService.sendInfo("【"+doc.getName() + "】被点赞",logId);

        //rocketMQTemplate.convertAndSend("VOTE_TOPIC","【"+doc.getName() + "】被点赞");
    }

    /**
     * 更新电子书信息
     */
    public void updateEbookInfo(){
        docMapperCust.updateEbookInfo();
    }

}
