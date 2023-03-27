package com.gd.upmms;

import com.gd.upmms.filter.MyStringArgumentResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import java.util.List;


/**
 * @author neusoft
 * @version 1.0
 * @project ruiji
 * @description SpringBoot启动类
 * @date 2022/11/17 16:06:59
 */

@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class UpmmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(UpmmsApplication.class,args);
        log.info("启动完成!");
    }

}
