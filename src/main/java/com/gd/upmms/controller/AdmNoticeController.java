package com.gd.upmms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gd.upmms.common.BaseContext;
import com.gd.upmms.common.R;
import com.gd.upmms.entity.AdmNotice;
import com.gd.upmms.service.AdmNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/adm/notice")
public class AdmNoticeController {


    @Autowired
    private AdmNoticeService admNoticeService;

    @RequestMapping("/getByCurrentId")
    public R<List<AdmNotice>> getByCurrentId(){
        LambdaQueryWrapper<AdmNotice> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(AdmNotice::getCreateTime);
        queryWrapper.eq(AdmNotice::getReceiverId, BaseContext.getCurrentId());
        List<AdmNotice> admNotices = admNoticeService.list(queryWrapper);
        return R.success(admNotices);
    }
}
