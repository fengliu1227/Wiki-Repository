package com.andrew.wiki.controller;

import com.andrew.wiki.request.DocQueryRequest;
import com.andrew.wiki.request.DocSaveRequest;
import com.andrew.wiki.response.DocQueryResponse;
import com.andrew.wiki.response.CommonResponse;
import com.andrew.wiki.response.PageResponse;
import com.andrew.wiki.service.DocService;
import com.andrew.wiki.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class DocController {

    @Autowired
    private UserService userservice;
    @Autowired
    private DocService docService;

    @GetMapping("/doc")
    public CommonResponse getAll(){
        CommonResponse<List> commonResponse = new CommonResponse<>();
        List<DocQueryResponse> list = docService.getAll();
        commonResponse.setContent(list);
        return commonResponse;
    }

    @GetMapping("/doc/list")
    public CommonResponse getList(@Valid DocQueryRequest req){
        CommonResponse<PageResponse> commonResponse = new CommonResponse<>();
        PageResponse<DocQueryResponse> list = docService.getList(req);
        commonResponse.setContent(list);
        return commonResponse;
    }

    @PutMapping("/doc")
    public CommonResponse update(@RequestBody DocSaveRequest req){
        CommonResponse commonResponse = new CommonResponse<>();
        docService.update(req);
        return commonResponse;
    }

    @PostMapping("/doc")
    public CommonResponse save(@RequestBody DocSaveRequest req){
        System.out.println("=====================================" + req);
        CommonResponse commonResponse = new CommonResponse<>();
        docService.save(req);
        return commonResponse;
    }

    @DeleteMapping("/doc/{id}")
    public CommonResponse delete(@PathVariable("id") Long id){
        CommonResponse commonResponse = new CommonResponse<>();
        docService.delete(id);
        return commonResponse;
    }
}
