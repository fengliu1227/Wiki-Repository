package com.andrew.wiki.job;

import com.andrew.wiki.service.DocService;
import com.andrew.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class DocJob {

    private static final Logger LOG = LoggerFactory.getLogger(DocJob.class);

    @Resource
    private DocService docService;

    @Resource
    private SnowFlake snowFlake;

    /**
     * 00:00:00 per day => update EBook info
     */
    @Scheduled(cron = "0 0 00 * * ? ")
    public void cron() {
        //Increase log serial number
        MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));
        LOG.info("update t_Ebook data start");
        long start = System.currentTimeMillis();
        docService.updateEbookInfo();
        LOG.info("update t_Ebook data，time consuming：{}ms", System.currentTimeMillis() - start);
    }

}
