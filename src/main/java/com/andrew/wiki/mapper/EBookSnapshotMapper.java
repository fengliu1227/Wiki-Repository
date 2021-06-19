package com.andrew.wiki.mapper;

import com.andrew.wiki.domain.EBookSnapshot;
import com.andrew.wiki.domain.EBookSnapshotExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EBookSnapshotMapper {
    long countByExample(EBookSnapshotExample example);

    int deleteByExample(EBookSnapshotExample example);

    int deleteByPrimaryKey(Long id);

    int insert(EBookSnapshot record);

    int insertSelective(EBookSnapshot record);

    List<EBookSnapshot> selectByExample(EBookSnapshotExample example);

    EBookSnapshot selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") EBookSnapshot record, @Param("example") EBookSnapshotExample example);

    int updateByExample(@Param("record") EBookSnapshot record, @Param("example") EBookSnapshotExample example);

    int updateByPrimaryKeySelective(EBookSnapshot record);

    int updateByPrimaryKey(EBookSnapshot record);

    void genSnapShot();
}