package com.andrew.wiki.service;

import com.andrew.wiki.domain.EBook;
import com.andrew.wiki.domain.EBookExample;
import com.andrew.wiki.mapper.EBookMapper;
import com.andrew.wiki.response.EBookResponse;
import com.andrew.wiki.util.CopyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EBookService {

    @Autowired
    private EBookMapper eBookMapper;

    public List<EBookResponse> getAll(){
        List<EBook> list = eBookMapper.selectByExample(new EBookExample());
        List<EBookResponse> resList = CopyUtil.copyList(list, EBookResponse.class);
        return resList;
    }

    public List<EBookResponse> searchByKeyword(String keyword){
        EBookExample eBookExample = new EBookExample();
        EBookExample.Criteria criteria = eBookExample.createCriteria();
        criteria.andNameLike("%" + keyword + "%");
        List<EBook> list = eBookMapper.selectByExample(eBookExample);

        List<EBookResponse> resList = CopyUtil.copyList(list, EBookResponse.class);
        return resList;
    }
}
