package com.gd.upmms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
@EnableScheduling//开启定时任务
public class UpmmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(UpmmsApplication.class,args);
        log.info("启动完成!");
    }

}
