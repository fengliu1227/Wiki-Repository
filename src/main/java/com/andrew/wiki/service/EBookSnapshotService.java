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
     * Get the numerical data of the homepage: total readings, total likes, readings today, likes today, estimated readings today, estimated reading growth
     */
    public List<StatisticResponse> getStatistic() {
        List<StatisticResponse> list = eBookSnapshotcustMapper.getStatistic();
        System.out.println("=========="+list.get(0).getDate().getTime());
        return list;
    }

    /**
     * 30-day numerical statistics
     */
    public List<StatisticResponse> get30Statistic() {
        return eBookSnapshotcustMapper.get30Statistic();
    }

}

