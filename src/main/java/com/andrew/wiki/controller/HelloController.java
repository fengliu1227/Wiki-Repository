package com.andrew.wiki.controller;

import com.andrew.wiki.domain.EBook;
import com.andrew.wiki.domain.User;
import com.andrew.wiki.request.EBookRequest;
import com.andrew.wiki.response.CommonResponse;
import com.andrew.wiki.response.EBookResponse;
import com.andrew.wiki.response.PageResponse;
import com.andrew.wiki.service.EBookService;
import com.andrew.wiki.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public CommonResponse getList(EBookRequest req){
        CommonResponse<PageResponse> commonResponse = new CommonResponse<>();
        PageResponse<EBookResponse> list = eBookService.getList(req);
        commonResponse.setContent(list);
        return commonResponse;
    }
}
