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

import javax.validation.Valid;
import java.util.List;

@RestController
public class EBookController {

    @Autowired
    private EBookService eBookService;


    @GetMapping("/ebook/list")
    public CommonResponse getList(@Valid EBookQueryRequest req){
        CommonResponse<PageResponse> commonResponse = new CommonResponse<>();
        PageResponse<EBookQueryResponse> list = eBookService.getList(req);
        commonResponse.setContent(list);
        return commonResponse;
    }

    @PutMapping("/ebook")
    public CommonResponse update(@RequestBody EBookSaveRequest req){
        CommonResponse commonResponse = new CommonResponse<>();
        eBookService.update(req);
        return commonResponse;
    }

    @PostMapping("/ebook")
    public CommonResponse save(@RequestBody EBookSaveRequest req){
        CommonResponse commonResponse = new CommonResponse<>();
        eBookService.save(req);
        return commonResponse;
    }

    @DeleteMapping("/ebook/{id}")
    public CommonResponse delete(@PathVariable("id") Long id){
        CommonResponse commonResponse = new CommonResponse<>();
        eBookService.delete(id);
        return commonResponse;
    }
}
