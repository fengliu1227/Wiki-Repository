package com.andrew.wiki.service;

import com.andrew.wiki.domain.User;
import com.andrew.wiki.domain.UserExample;
import com.andrew.wiki.exception.BusinessException;
import com.andrew.wiki.exception.BusinessExceptionCode;
import com.andrew.wiki.mapper.UserMapper;
import com.andrew.wiki.request.UserLoginRequest;
import com.andrew.wiki.request.UserQueryRequest;
import com.andrew.wiki.request.UserSaveRequest;
import com.andrew.wiki.request.UserSetPassRequest;
import com.andrew.wiki.response.UserLoginResponse;
import com.andrew.wiki.response.UserQueryResponse;
import com.andrew.wiki.response.PageResponse;
import com.andrew.wiki.util.CopyUtil;
import com.andrew.wiki.util.SnowFlake;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SnowFlake snowFlake;

    public List<UserQueryResponse> getAll(){
        UserExample userExample = new UserExample();
//        UserExample.Criteria criteria = UserExample.createCriteria();
        List<User> list = userMapper.selectByExample(userExample);


        List<UserQueryResponse> resList = CopyUtil.copyList(list, UserQueryResponse.class);
        return resList;
    }

    public PageResponse<UserQueryResponse> getList(UserQueryRequest req){
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if(!ObjectUtils.isEmpty(req.getLoginName())){
            criteria.andLoginNameEqualTo(req.getLoginName());
        }
        PageHelper.startPage(req.getPage(), req.getSize());
        List<User> list = userMapper.selectByExample(userExample);

        PageInfo<User> pageInfo = new PageInfo<>(list);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<UserQueryResponse> resList = CopyUtil.copyList(list, UserQueryResponse.class);
        PageResponse<UserQueryResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(resList);
        return pageResponse;
    }

    public void update(UserSaveRequest req){
        User user = CopyUtil.copy(req, User.class);
        user.setLoginName(null);
        user.setPassword(null);
        userMapper.updateByPrimaryKeySelective(user);
    }

    public void save(UserSaveRequest req){
        if(ObjectUtils.isEmpty(selectByLoginName(req.getLoginName()))){
            User user = CopyUtil.copy(req, User.class);
            user.setId(snowFlake.nextId());
            user.setRole("ROLE_USER");
            userMapper.insert(user);
        }else{
            throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
        }
    }

    public void delete(Long id){
        userMapper.deleteByPrimaryKey(id);
    }

    public User selectByLoginName(String LoginName){
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andLoginNameEqualTo(LoginName);
        List<User> list = userMapper.selectByExample(userExample);
        if(CollectionUtils.isEmpty(list)){
            return null;
        }else{
            return list.get(0);
        }
    }

    public void updatePassWord(UserSetPassRequest req, Long id){
        User user = new User();
        user.setId(id);
        user.setPassword(req.getPassword());
        userMapper.updateByPrimaryKeySelective(user);
    }

    public UserLoginResponse login(UserLoginRequest req){
        User userDb = selectByLoginName(req.getLoginName());
        if(ObjectUtils.isEmpty(userDb)){
            LOG.info("LoginName is not exist, {}", req.getLoginName());
            throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
        }else{
            if(userDb.getPassword().equals(req.getPassword())){
                UserLoginResponse userLoginResponse = CopyUtil.copy(userDb, UserLoginResponse.class);
                return userLoginResponse;
            }else{
                LOG.info("Password is not correct, LoginName: {}, Password:{}", req.getLoginName() , userDb.getPassword());
                throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
            }
        }
    }
}
