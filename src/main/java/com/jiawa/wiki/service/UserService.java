package com.jiawa.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.wiki.domain.User;
import com.jiawa.wiki.domain.UserExample;
import com.jiawa.wiki.exception.BusinessException;
import com.jiawa.wiki.exception.BusinessExceptionCode;
import com.jiawa.wiki.mapper.UserMapper;
import com.jiawa.wiki.req.UserQueryReq;
import com.jiawa.wiki.req.UserSaveReq;
import com.jiawa.wiki.resp.UserQueryResp;
import com.jiawa.wiki.resp.PageResp;
import com.jiawa.wiki.utils.CopyUtils;
import com.jiawa.wiki.utils.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
public class UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserMapper userMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResp<UserQueryResp> list(UserQueryReq userQueryReq){
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if (!ObjectUtils.isEmpty(userQueryReq.getLoginName())) {
            criteria.andLoginNameEqualTo(userQueryReq.getLoginName());
        }
        PageHelper.startPage(userQueryReq.getPage(), userQueryReq.getSize());
        List<User> userList = userMapper.selectByExample(userExample);
        LOG.info("EEEEEEEEUserlist:{}",userList);

        PageInfo<User> pageInfo = new PageInfo<>(userList);
        // 总行数
        LOG.info("总行数:{}",pageInfo.getTotal());
        // 总页数
        LOG.info("总行数:{}",pageInfo.getPages());

        // List<UserResp> respList = new ArrayList<>();
        // for (User user : userList) {
        //     // UserResp userResp = new UserResp();
        //     // BeanUtils.copyProperties(user, userResp);
        //
        //     UserResp userResp = CopyUtils.copy(user, UserResp.class);
        //     respList.add(userResp);
        // }

        List<UserQueryResp> respList = CopyUtils.copyList(userList, UserQueryResp.class);

        PageResp<UserQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);

        LOG.info("PPPPPageResp:{}",pageResp);

        return pageResp;
    }

    /**
     * 保存，由id判断是更新还是新增
     */
    public void save(UserSaveReq userSaveReq){
        User user = CopyUtils.copy(userSaveReq, User.class);
        if(ObjectUtils.isEmpty(userSaveReq.getId())){
            if(ObjectUtils.isEmpty(selectByLoginName(userSaveReq.getLoginName()))){
                // 新增
                user.setId(snowFlake.nextId());
                userMapper.insert(user);
            }else{
                // 用户名已存在
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }

        }else{
            user.setLoginName(null);
            user.setPassword(null);
            // 更新 Selective当user内有值才更新
            userMapper.updateByPrimaryKeySelective(user);
        }
    }

    public void delete(Long id){
        userMapper.deleteByPrimaryKey(id);
    }

    public User selectByLoginName(String loginName){
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andLoginNameEqualTo(loginName);
        List<User> userList = userMapper.selectByExample(userExample);
        if(CollectionUtils.isEmpty(userList)){
            return null;
        }else{
            return userList.get(0);
        }
    }
}
