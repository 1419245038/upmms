package com.gd.upmms.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gd.upmms.common.BaseContext;
import com.gd.upmms.common.CustomException;
import com.gd.upmms.common.R;
import com.gd.upmms.entity.AdmPartUser;
import com.gd.upmms.entity.AdmRecord;
import com.gd.upmms.service.AdmPartUserService;
import com.gd.upmms.service.AdmRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/adm/record")
public class AdmRecordController {

    @Autowired
    private AdmRecordService admRecordService;

    @Autowired
    private AdmPartUserService admPartUserService;

    @RequestMapping("/add")
    public R<String> add(AdmRecord admRecord){
        admRecord.setUserId(BaseContext.getCurrentId());
        LambdaQueryWrapper<AdmPartUser> partUserLambdaQueryWrapper=new LambdaQueryWrapper<>();
        partUserLambdaQueryWrapper.eq(AdmPartUser::getUserId,BaseContext.getCurrentId());
        AdmPartUser partUser = admPartUserService.getOne(partUserLambdaQueryWrapper);
        if (partUser==null){
            throw new CustomException("您不需要缴纳党费");
        }
        boolean save = admRecordService.save(admRecord);
        return save?R.success("保存成功!"):R.error("保存失败!");
    }

    @RequestMapping("/getByCurrentId")
    public R<List<AdmRecord>> getByCurrentId(){
        LambdaQueryWrapper<AdmRecord> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(AdmRecord::getUserId,BaseContext.getCurrentId());
        List<AdmRecord> list = admRecordService.list(queryWrapper);
        return R.success(list);
    }

    @RequestMapping("/search")
    public R<List<AdmRecord>> search(String payment){
        LambdaQueryWrapper<AdmRecord> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(StrUtil.isNotEmpty(payment),AdmRecord::getPayment,payment);
        List<AdmRecord> list = admRecordService.list(queryWrapper);
        return R.success(list);
    }
}
