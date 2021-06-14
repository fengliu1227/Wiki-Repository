package com.andrew.wiki.service;

import com.andrew.wiki.domain.EBook;
import com.andrew.wiki.domain.EBookExample;
import com.andrew.wiki.mapper.EBookMapper;
import com.andrew.wiki.request.EBookRequest;
import com.andrew.wiki.response.EBookResponse;
import com.andrew.wiki.response.PageResponse;
import com.andrew.wiki.util.CopyUtil;
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

    public PageResponse<EBookResponse> getList(EBookRequest req){
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

        List<EBookResponse> resList = CopyUtil.copyList(list, EBookResponse.class);
        PageResponse<EBookResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(resList);
        return pageResponse;
    }

}
