package com.jiawa.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.wiki.domain.Category;
import com.jiawa.wiki.domain.CategoryExample;
import com.jiawa.wiki.mapper.CategoryMapper;
import com.jiawa.wiki.req.CategoryQueryReq;
import com.jiawa.wiki.req.CategorySaveReq;
import com.jiawa.wiki.resp.CategoryQueryResp;
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
public class CategoryService {
    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResp<CategoryQueryResp> list(CategoryQueryReq categoryQueryReq){
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        PageHelper.startPage(categoryQueryReq.getPage(), categoryQueryReq.getSize());
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        LOG.info("EEEEEEEECategorylist:{}",categoryList);

        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        // 总行数
        LOG.info("总行数:{}",pageInfo.getTotal());
        // 总页数
        LOG.info("总行数:{}",pageInfo.getPages());

        // List<CategoryResp> respList = new ArrayList<>();
        // for (Category category : categoryList) {
        //     // CategoryResp categoryResp = new CategoryResp();
        //     // BeanUtils.copyProperties(category, categoryResp);
        //
        //     CategoryResp categoryResp = CopyUtils.copy(category, CategoryResp.class);
        //     respList.add(categoryResp);
        // }

        List<CategoryQueryResp> respList = CopyUtils.copyList(categoryList, CategoryQueryResp.class);

        PageResp<CategoryQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);

        LOG.info("PPPPPageResp:{}",pageResp);

        return pageResp;
    }

    /**
     * 保存，由id判断是更新还是新增
     */
    public void save(CategorySaveReq categorySaveReq){
        LOG.info("CategorySaveReq.toString():{}" ,categorySaveReq.toString());
        Category category = CopyUtils.copy(categorySaveReq, Category.class);
        if(ObjectUtils.isEmpty(categorySaveReq.getId())){
            // 新增
            category.setId(snowFlake.nextId());
            categoryMapper.insert(category);
        }else{
            // 更新
            categoryMapper.updateByPrimaryKey(category);
        }
    }

    public void delete(Long id){
        categoryMapper.deleteByPrimaryKey(id);
    }
}
