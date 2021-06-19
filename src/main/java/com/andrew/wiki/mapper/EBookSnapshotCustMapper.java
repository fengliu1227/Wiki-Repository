package com.andrew.wiki.mapper;

import com.andrew.wiki.domain.EBookSnapshot;
import com.andrew.wiki.domain.EBookSnapshotExample;
import com.andrew.wiki.response.StatisticResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EBookSnapshotCustMapper {
    void genSnapShot();

    List<StatisticResponse> getStatistic();

    List<StatisticResponse> get30Statistic();
}