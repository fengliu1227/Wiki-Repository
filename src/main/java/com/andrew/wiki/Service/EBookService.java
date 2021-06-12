package com.andrew.wiki.Service;

import com.andrew.wiki.Mapper.EBookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EBookService {

    @Autowired
    private EBookMapper eBookMapper;
}
