package com.andrew.wiki.service;

import com.andrew.wiki.domain.EBook;
import com.andrew.wiki.domain.EBookExample;
import com.andrew.wiki.mapper.EBookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EBookService {

    @Autowired
    private EBookMapper eBookMapper;

    public List<EBook> getAll(){
        return eBookMapper.selectByExample(new EBookExample());
    }
}
