package com.andrew.wiki.service;

import com.andrew.wiki.mapper.EBookSnapshotCustMapper;
import com.andrew.wiki.response.StatisticResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class EBookSnapshotService {

    @Autowired
    EBookSnapshotCustMapper eBookSnapshotcustMapper;

    public void genSnapShot(){
        eBookSnapshotcustMapper.genSnapShot();
    }

    /**
     * 获取首页数值数据：总阅读数、总点赞数、今日阅读数、今日点赞数、今日预计阅读数、今日预计阅读增长
     */
    public List<StatisticResponse> getStatistic() {
        List<StatisticResponse> list = eBookSnapshotcustMapper.getStatistic();
        System.out.println("=========="+list.get(0).getDate().getTime());
        return list;
    }

    /**
     * 30天数值统计
     */
    public List<StatisticResponse> get30Statistic() {
        return eBookSnapshotcustMapper.get30Statistic();
    }

}

