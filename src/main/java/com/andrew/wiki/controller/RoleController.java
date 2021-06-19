package com.andrew.wiki.controller;

import com.andrew.wiki.response.CommonResponse;
import com.andrew.wiki.response.RoleResponse;
import com.andrew.wiki.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/role")
    public CommonResponse getAll(){
        CommonResponse commonResponse = new CommonResponse();
        List<RoleResponse> all = roleService.getAll();
        commonResponse.setContent(all);
        return commonResponse;
    }
}
