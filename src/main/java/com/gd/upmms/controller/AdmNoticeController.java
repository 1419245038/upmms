package com.gd.upmms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gd.upmms.common.BaseContext;
import com.gd.upmms.common.R;
import com.gd.upmms.entity.AdmNotice;
import com.gd.upmms.entity.AdmPartUser;
import com.gd.upmms.entity.SysRoleUser;
import com.gd.upmms.service.AdmNoticeService;
import com.gd.upmms.service.AdmPartUserService;
import com.gd.upmms.service.SysRoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/adm/notice")
public class AdmNoticeController {


    @Autowired
    private AdmNoticeService admNoticeService;

    @Autowired
    private SysRoleUserService sysRoleUserService;

    @Autowired
    private AdmPartUserService admPartUserService;

    @RequestMapping("/getByCurrentId")
    public R<List<AdmNotice>> getByCurrentId(){

        List<AdmNotice> admNotices;

        LambdaQueryWrapper<AdmNotice> admNoticeLambdaQueryWrapper=new LambdaQueryWrapper<>();
        admNoticeLambdaQueryWrapper.orderByDesc(AdmNotice::getCreateTime);
        admNoticeLambdaQueryWrapper.eq(AdmNotice::getReceiverId, BaseContext.getCurrentId());
        LambdaQueryWrapper<SysRoleUser> sysRoleUserLambdaQueryWrapper=new LambdaQueryWrapper<>();
        sysRoleUserLambdaQueryWrapper.eq(SysRoleUser::getUserId,BaseContext.getCurrentId());
        SysRoleUser roleUser = sysRoleUserService.getOne(sysRoleUserLambdaQueryWrapper);
        if (roleUser.getRoleId()==12){
            LambdaQueryWrapper<AdmPartUser> admPartUserLambdaQueryWrapper=new LambdaQueryWrapper<>();
            admPartUserLambdaQueryWrapper.eq(AdmPartUser::getUserId,BaseContext.getCurrentId());
            AdmPartUser partUser = admPartUserService.getOne(admPartUserLambdaQueryWrapper);
            LambdaQueryWrapper<AdmNotice> admNoticeLambdaQueryWrapper1=new LambdaQueryWrapper<>();
            admNoticeLambdaQueryWrapper1.eq(AdmNotice::getPartId,partUser.getPartId());
            admNotices = admNoticeService.list(admNoticeLambdaQueryWrapper1);
        }else {
            admNotices = admNoticeService.list(admNoticeLambdaQueryWrapper);
        }

        return R.success(admNotices);
    }
}
