package com.andrew.wiki.controller;

import com.andrew.wiki.domain.User;
import com.andrew.wiki.request.CategoryQueryRequest;
import com.andrew.wiki.request.CategorySaveRequest;
import com.andrew.wiki.response.CommonResponse;
import com.andrew.wiki.response.CategoryQueryResponse;
import com.andrew.wiki.response.PageResponse;
import com.andrew.wiki.service.CategoryService;
import com.andrew.wiki.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private UserService userservice;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/category")
    public CommonResponse getAll(){
        CommonResponse<List> commonResponse = new CommonResponse<>();
        List<CategoryQueryResponse> list = categoryService.getAll();
        commonResponse.setContent(list);
        return commonResponse;
    }

    @GetMapping("/category/list")
    public CommonResponse getList(@Valid CategoryQueryRequest req){
        CommonResponse<PageResponse> commonResponse = new CommonResponse<>();
        PageResponse<CategoryQueryResponse> list = categoryService.getList(req);
        commonResponse.setContent(list);
        return commonResponse;
    }

    @PutMapping("/category")
    public CommonResponse update(@RequestBody CategorySaveRequest req){
        CommonResponse commonResponse = new CommonResponse<>();
        categoryService.update(req);
        return commonResponse;
    }

    @PostMapping("/category")
    public CommonResponse save(@RequestBody CategorySaveRequest req){
        CommonResponse commonResponse = new CommonResponse<>();
        categoryService.save(req);
        return commonResponse;
    }

    @DeleteMapping("/category/{id}")
    public CommonResponse delete(@PathVariable("id") Long id){
        CommonResponse commonResponse = new CommonResponse<>();
        categoryService.delete(id);
        return commonResponse;
    }
}
