package com.andrew.wiki.controller;

import com.andrew.wiki.mapper.DocCustMapper;
import com.andrew.wiki.response.CommonResponse;
import com.andrew.wiki.response.ContentResponse;
import com.andrew.wiki.response.PageResponse;
import com.andrew.wiki.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContentController {

    @Autowired
    ContentService contentService;


    @GetMapping("/content/{id}")
    public CommonResponse getById(@PathVariable("id") Long id){
        CommonResponse<ContentResponse> commonResponse = new CommonResponse<>();
        ContentResponse contentResponse = contentService.getById(id);
        commonResponse.setContent(contentResponse);
        return commonResponse;
    }
}
