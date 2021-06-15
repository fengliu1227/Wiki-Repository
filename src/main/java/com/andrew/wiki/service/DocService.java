package com.andrew.wiki.service;

import com.andrew.wiki.domain.Content;
import com.andrew.wiki.domain.ContentExample;
import com.andrew.wiki.domain.Doc;
import com.andrew.wiki.domain.DocExample;
import com.andrew.wiki.mapper.ContentMapper;
import com.andrew.wiki.mapper.DocMapper;
import com.andrew.wiki.request.DocQueryRequest;
import com.andrew.wiki.request.DocSaveRequest;
import com.andrew.wiki.response.DocQueryResponse;
import com.andrew.wiki.response.PageResponse;
import com.andrew.wiki.util.CopyUtil;
import com.andrew.wiki.util.SnowFlake;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocService {

    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

    @Autowired
    private DocMapper docMapper;

    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private SnowFlake snowFlake;

    public List<DocQueryResponse> getAll(){
        DocExample docExample = new DocExample();
//        DocExample.Criteria criteria = docExample.createCriteria();
        docExample.setOrderByClause("sort asc");
        List<Doc> list = docMapper.selectByExample(docExample);


        List<DocQueryResponse> resList = CopyUtil.copyList(list, DocQueryResponse.class);
        return resList;
    }

    public PageResponse<DocQueryResponse> getList(DocQueryRequest req){
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
//        DocExample.Criteria criteria = docExample.createCriteria();
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Doc> list = docMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(list);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<DocQueryResponse> resList = CopyUtil.copyList(list, DocQueryResponse.class);
        PageResponse<DocQueryResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(resList);
        return pageResponse;
    }

    public List<DocQueryResponse> getByEbookId(Long id){
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        docExample.setOrderByClause("sort asc");
        criteria.andEbookIdEqualTo(id);
        List<Doc> list = docMapper.selectByExample(docExample);


        List<DocQueryResponse> resList = CopyUtil.copyList(list, DocQueryResponse.class);
        return resList;
    }

    public void update(DocSaveRequest req){
        Doc doc = CopyUtil.copy(req, Doc.class);
        Content content = CopyUtil.copy(req, Content.class);
        docMapper.updateByPrimaryKey(doc);
        int count = contentMapper.updateByPrimaryKeyWithBLOBs(content);
        if(count < 1){
            contentMapper.insert(content);
        }
    }

    public void save(DocSaveRequest req){
        Doc doc = CopyUtil.copy(req, Doc.class);
        Content content = CopyUtil.copy(req, Content.class);
        doc.setId(snowFlake.nextId());
        content.setId(doc.getId());
        docMapper.insert(doc);
        contentMapper.insert(content);

    }

    public void delete(Long id){
        docMapper.deleteByPrimaryKey(id);
    }

    public void delete(List<String> ids){
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);
        ContentExample contentExample = new ContentExample();
        ContentExample.Criteria contentCriteria = contentExample.createCriteria();;
        contentCriteria.andIdIn(ids);
        contentMapper.deleteByExample(contentExample);
    }

}
