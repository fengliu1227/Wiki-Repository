package com.andrew.wiki.service;

import com.andrew.wiki.domain.Role;
import com.andrew.wiki.domain.RoleExample;
import com.andrew.wiki.mapper.RoleMapper;
import com.andrew.wiki.response.RoleResponse;
import com.andrew.wiki.util.CopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;

    public List<RoleResponse> getAll(){
        List<Role> roles = roleMapper.selectByExample(new RoleExample());
        List<RoleResponse> res = CopyUtil.copyList(roles, RoleResponse.class);
        return res;
    }
}
