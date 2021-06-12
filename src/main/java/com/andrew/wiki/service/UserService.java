package com.andrew.wiki.service;

import com.andrew.wiki.domain.User;
import com.andrew.wiki.domain.UserExample;
import com.andrew.wiki.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {


    @Autowired
    private UserMapper userMapper;

    public List<User> getAll(){
        return userMapper.selectByExample(new UserExample());
    }
}
