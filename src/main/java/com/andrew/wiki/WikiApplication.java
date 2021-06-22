package com.andrew.wiki;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.andrew.wiki.mapper")
@EnableScheduling
@EnableAsync
public class WikiApplication {

    private static final Logger LOG = LoggerFactory.getLogger(WikiApplication.class);
    public static void main(String[] args) {
//        SpringApplication.run(WikiApplication.class, args);
        SpringApplication app = new SpringApplication(WikiApplication.class);
        Environment env = app.run(args).getEnvironment();
        LOG.info("Run Application Successfully");
        LOG.info("url: \t http://localhost:{}", env.getProperty("server.port"));
        LOG.info(env.getProperty("spring.datasource.url"));
    }

}
