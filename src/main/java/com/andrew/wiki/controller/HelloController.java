package com.andrew.wiki.controller;

import com.andrew.wiki.domain.User;
import com.andrew.wiki.request.EBookQueryRequest;
import com.andrew.wiki.request.EBookSaveRequest;
import com.andrew.wiki.response.CommonResponse;
import com.andrew.wiki.response.EBookQueryResponse;
import com.andrew.wiki.response.PageResponse;
import com.andrew.wiki.service.EBookService;
import com.andrew.wiki.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HelloController {

    @Autowired
    private UserService userservice;
    @Autowired
    private EBookService eBookService;
    @GetMapping("/hello")
    public List<User> hello(){
        return userservice.getAll();
    }

    @GetMapping("/ebook/list")
    public CommonResponse getList(EBookQueryRequest req){
        CommonResponse<PageResponse> commonResponse = new CommonResponse<>();
        PageResponse<EBookQueryResponse> list = eBookService.getList(req);
        commonResponse.setContent(list);
        return commonResponse;
    }

    @PutMapping("/ebook/{id}")
    public CommonResponse update(@PathVariable("id")Long id, @RequestBody EBookSaveRequest req){
        req.setId(id);
        CommonResponse commonResponse = new CommonResponse<>();
        eBookService.update(req);
        return commonResponse;
    }
}
