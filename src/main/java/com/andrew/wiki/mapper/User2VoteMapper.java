package com.andrew.wiki.mapper;

import com.andrew.wiki.domain.User2Vote;
import com.andrew.wiki.domain.User2VoteExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface User2VoteMapper {
    long countByExample(User2VoteExample example);

    int deleteByExample(User2VoteExample example);

    int deleteByPrimaryKey(Long id);

    int insert(User2Vote record);

    int insertSelective(User2Vote record);

    List<User2Vote> selectByExample(User2VoteExample example);

    User2Vote selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") User2Vote record, @Param("example") User2VoteExample example);

    int updateByExample(@Param("record") User2Vote record, @Param("example") User2VoteExample example);

    int updateByPrimaryKeySelective(User2Vote record);

    int updateByPrimaryKey(User2Vote record);
}