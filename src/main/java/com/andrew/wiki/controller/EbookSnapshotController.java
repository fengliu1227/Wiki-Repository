package com.andrew.wiki.controller;

import com.andrew.wiki.response.CommonResponse;
import com.andrew.wiki.response.StatisticResponse;
import com.andrew.wiki.service.EBookSnapshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class EbookSnapshotController {

    @Autowired
    private EBookSnapshotService ebookSnapshotService;

    @GetMapping("/statistic")
    public CommonResponse getStatistic() {
        List<StatisticResponse> statisticResponse = ebookSnapshotService.getStatistic();
        CommonResponse<List<StatisticResponse>> commonResponse = new CommonResponse<>();
        commonResponse.setContent(statisticResponse);
        return commonResponse;
    }

    @GetMapping("/statistic/month")
    public CommonResponse get30Statistic() {
        List<StatisticResponse> statisticResponse = ebookSnapshotService.get30Statistic();
        CommonResponse<List<StatisticResponse>> commonResponse = new CommonResponse<>();
        commonResponse.setContent(statisticResponse);
        return commonResponse;
    }

}
