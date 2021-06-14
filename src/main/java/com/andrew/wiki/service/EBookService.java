package com.andrew.wiki.service;

import com.andrew.wiki.domain.EBook;
import com.andrew.wiki.domain.EBookExample;
import com.andrew.wiki.mapper.EBookMapper;
import com.andrew.wiki.request.EBookQueryRequest;
import com.andrew.wiki.request.EBookSaveRequest;
import com.andrew.wiki.response.CommonResponse;
import com.andrew.wiki.response.EBookQueryResponse;
import com.andrew.wiki.response.PageResponse;
import com.andrew.wiki.util.CopyUtil;
import com.andrew.wiki.util.SnowFlake;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import java.util.List;

@Service
public class EBookService {

    private static final Logger LOG = LoggerFactory.getLogger(EBookService.class);

    @Autowired
    private EBookMapper eBookMapper;

    @Autowired
    private SnowFlake snowFlake;

    public PageResponse<EBookQueryResponse> getList(EBookQueryRequest req){
        EBookExample eBookExample = new EBookExample();
        EBookExample.Criteria criteria = eBookExample.createCriteria();
        if(!ObjectUtils.isEmpty(req.getName())){
            criteria.andNameLike("%" + req.getName() + "%");
        }

        PageHelper.startPage(req.getPage(), req.getSize());
        List<EBook> list = eBookMapper.selectByExample(eBookExample);

        PageInfo<EBook> pageInfo = new PageInfo<>(list);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<EBookQueryResponse> resList = CopyUtil.copyList(list, EBookQueryResponse.class);
        PageResponse<EBookQueryResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(resList);
        return pageResponse;
    }

    public void update(EBookSaveRequest req){
        EBook ebook = CopyUtil.copy(req, EBook.class);
        eBookMapper.updateByPrimaryKey(ebook);
    }

    public void save(EBookSaveRequest req){
        EBook ebook = CopyUtil.copy(req, EBook.class);
        ebook.setId(snowFlake.nextId());
        eBookMapper.insert(ebook);
    }

    public void delete(Long id){
        eBookMapper.deleteByPrimaryKey(id);
    }

}
