package com.andrew.wiki.mapper;

import org.apache.ibatis.annotations.Param;


public interface DocCustMapper {
    void increaseViewCount(@Param("id") Long id);
    void increaseVoteCount(@Param("id") Long id);
    void updateEbookInfo();
}