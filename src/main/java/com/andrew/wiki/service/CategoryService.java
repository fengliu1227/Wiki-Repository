package com.andrew.wiki.service;

import com.andrew.wiki.domain.Category;
import com.andrew.wiki.domain.CategoryExample;
import com.andrew.wiki.mapper.CategoryMapper;
import com.andrew.wiki.request.CategoryQueryRequest;
import com.andrew.wiki.request.CategorySaveRequest;
import com.andrew.wiki.response.CategoryQueryResponse;
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
public class CategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SnowFlake snowFlake;

    public List<CategoryQueryResponse> getAll(){
        CategoryExample categoryExample = new CategoryExample();
//        CategoryExample.Criteria criteria = CategoryExample.createCriteria();
        categoryExample.setOrderByClause("sort asc");
        List<Category> list = categoryMapper.selectByExample(categoryExample);


        List<CategoryQueryResponse> resList = CopyUtil.copyList(list, CategoryQueryResponse.class);
        return resList;
    }

    public PageResponse<CategoryQueryResponse> getList(CategoryQueryRequest req){
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
//        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Category> list = categoryMapper.selectByExample(categoryExample);

        PageInfo<Category> pageInfo = new PageInfo<>(list);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<CategoryQueryResponse> resList = CopyUtil.copyList(list, CategoryQueryResponse.class);
        PageResponse<CategoryQueryResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(resList);
        return pageResponse;
    }

    public void update(CategorySaveRequest req){
        Category category = CopyUtil.copy(req, Category.class);
        categoryMapper.updateByPrimaryKey(category);
    }

    public void save(CategorySaveRequest req){
        Category category = CopyUtil.copy(req, Category.class);
        category.setId(snowFlake.nextId());
        categoryMapper.insert(category);
    }

    public void delete(Long id){
        categoryMapper.deleteByPrimaryKey(id);
    }

}
