package com.andrew.wiki.job;

import com.andrew.wiki.service.EBookSnapshotService;
import com.andrew.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EBookSnapshotJob {

    private static final Logger LOG = LoggerFactory.getLogger(EBookSnapshotJob.class);

    @Autowired
    private EBookSnapshotService eBookSnapshotService;

    @Autowired
    private SnowFlake snowFlake;

    /**
     * 00:00:00 per day => update eBookSnapshot
     */
    @Scheduled(cron = "0 0 00 * * ? ")
    public void cron() {
        //Increase log serial number
        MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));
        LOG.info("update eBookSnapshot data start");
        long start = System.currentTimeMillis();
        eBookSnapshotService.genSnapShot();
        LOG.info("update eBookSnapshot data，time consuming：{}ms", System.currentTimeMillis() - start);
    }

}
