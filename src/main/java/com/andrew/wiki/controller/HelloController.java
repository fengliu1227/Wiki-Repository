package com.andrew.wiki.controller;

import com.andrew.wiki.domain.EBook;
import com.andrew.wiki.domain.User;
import com.andrew.wiki.response.CommonResponse;
import com.andrew.wiki.response.EBookResponse;
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

    @GetMapping("/books")
    public CommonResponse getAll(){
        CommonResponse<List> commonResponse = new CommonResponse<>();
        List<EBookResponse> list = eBookService.getAll();
        commonResponse.setContent(list);
        return commonResponse;
    }

    @GetMapping("/search")
    public CommonResponse search(EBookResponse req){
        CommonResponse<List> commonResponse = new CommonResponse<>();
        List<EBookResponse> list = eBookService.searchByKeyword(req);
        commonResponse.setContent(list);
        return commonResponse;
    }
}
