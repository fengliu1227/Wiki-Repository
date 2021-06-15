package com.andrew.wiki.service;

import com.andrew.wiki.domain.Content;
import com.andrew.wiki.domain.ContentExample;
import com.andrew.wiki.domain.Doc;
import com.andrew.wiki.domain.DocExample;
import com.andrew.wiki.mapper.ContentMapper;
import com.andrew.wiki.mapper.DocMapper;
import com.andrew.wiki.request.DocQueryRequest;
import com.andrew.wiki.request.DocSaveRequest;
import com.andrew.wiki.response.ContentResponse;
import com.andrew.wiki.response.DocQueryResponse;
import com.andrew.wiki.response.PageResponse;
import com.andrew.wiki.util.CopyUtil;
import com.andrew.wiki.util.SnowFlake;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentService {

    private static final Logger LOG = LoggerFactory.getLogger(ContentService.class);

    @Autowired
    private ContentMapper contentMapper;

    public ContentResponse getById(Long id){
        Content content = contentMapper.selectByPrimaryKey(id);
        ContentResponse contentResponse = CopyUtil.copy(content, ContentResponse.class);
        return contentResponse;
    }

}
