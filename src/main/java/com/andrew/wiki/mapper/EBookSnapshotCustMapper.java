package com.andrew.wiki.mapper;

import com.andrew.wiki.response.StatisticResponse;

import java.util.List;

public interface EBookSnapshotCustMapper {
    void genSnapShot();

    List<StatisticResponse> getStatistic();

    List<StatisticResponse> get30Statistic();
}