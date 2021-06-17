package com.andrew.wiki.mapper;

import org.apache.ibatis.annotations.Param;


public interface EBookCustMapper {
    void increaseViewCount(@Param("id") Long id);
    void increaseVoteCount(@Param("id") Long id);
    void increaseDocCount(@Param("id") Long id);
    void decreaseDocCount(@Param("nums")Integer nums, @Param("id")Long id);
}