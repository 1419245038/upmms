package com.gd.upmms.scheduling;

import cn.hutool.extra.mail.MailUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gd.upmms.entity.*;
import com.gd.upmms.service.AdmPartUserService;
import com.gd.upmms.service.AdmRecordService;
import com.gd.upmms.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class Remind {

    @Autowired
    private AdmPartUserService admPartUserService;

    @Autowired
    private AdmRecordService admRecordService;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 每月的20日上午10时定时提醒未缴纳党费用户
     */
    @Scheduled(cron="0 0 10 20 * ? ")
    public void send(){
        List<SysUser> unPayUsers = getUnPayUsers();
        for (SysUser unPayUser : unPayUsers) {
            String content="["+unPayUser.getUsername()+"]"+"同志您好,您本月党费还未缴纳，请及时缴纳党费。";
            MailUtil.send(unPayUser.getEmail(), "党费缴纳提醒", content, false);
        }
    }

    private List<SysUser> getUnPayUsers(){
        //党组织里面的所有用户id
        List<Integer> partUserIds=new ArrayList<>();
        //未缴费的用户id
        List<Integer> unPayUserIds=new ArrayList<>();
        //查询党党组织里的所有用户id
        List<AdmPartUser> partUsers = admPartUserService.list();
        for (AdmPartUser partUser : partUsers) {
            partUserIds.add(partUser.getUserId());
        }
        //获取本月未缴费的用户id
        String currentDate = new SimpleDateFormat("yyyy-MM").format(new Date());
        for (Integer partUserId : partUserIds) {
            LambdaQueryWrapper<AdmRecord> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(AdmRecord::getUserId,partUserId);
            queryWrapper.like(AdmRecord::getPayment,currentDate);
            int count = admRecordService.count(queryWrapper);
            if (count<1){
                unPayUserIds.add(partUserId);
            }
        }
        //获取未缴费的用户信息
        LambdaQueryWrapper<SysUser> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(SysUser::getUserId,unPayUserIds);
        return sysUserService.list(queryWrapper);
    }
}
