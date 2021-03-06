package com.andrew.wiki.controller;

import com.andrew.wiki.request.DocQueryRequest;
import com.andrew.wiki.request.DocSaveRequest;
import com.andrew.wiki.request.DocVoteRequest;
import com.andrew.wiki.response.DocQueryResponse;
import com.andrew.wiki.response.CommonResponse;
import com.andrew.wiki.response.PageResponse;
import com.andrew.wiki.service.DocService;
import com.andrew.wiki.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
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

    @GetMapping("/doc/all/{id}")
    public CommonResponse getByEbookId(@PathVariable("id")Long id){
        CommonResponse<List> commonResponse = new CommonResponse<>();
        List<DocQueryResponse> list = docService.getByEbookId(id);
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
        CommonResponse commonResponse = new CommonResponse<>();
        docService.save(req);
        return commonResponse;
    }

    @DeleteMapping("/doc/{idsStr}")
    public CommonResponse delete(@PathVariable("idsStr") String idsStr){
        CommonResponse commonResponse = new CommonResponse<>();
        List<String> list = Arrays.asList(idsStr.split(","));
        docService.delete(list);
        return commonResponse;
    }

    @PostMapping("/doc/vote")
    public CommonResponse vote(@RequestBody DocVoteRequest req){
        CommonResponse<DocQueryResponse> commonResponse = new CommonResponse<>();
        DocQueryResponse docQueryResponse = docService.vote(req);
        commonResponse.setContent(docQueryResponse);
        return commonResponse;
    }
    @GetMapping("/doc/vote")
    public CommonResponse isVoted(@RequestParam("userId")Long userId, @RequestParam("docId") Long docId){
        CommonResponse<Boolean> commonResponse = new CommonResponse<>();
        Boolean res = docService.isVoted(userId, docId);
        commonResponse.setContent(res);
        return commonResponse;
    }
}
