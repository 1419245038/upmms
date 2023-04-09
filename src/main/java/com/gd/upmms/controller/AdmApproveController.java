package com.gd.upmms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gd.upmms.common.BaseContext;
import com.gd.upmms.common.R;
import com.gd.upmms.entity.AdmApplyForm;
import com.gd.upmms.entity.AdmPartUser;
import com.gd.upmms.service.AdmApplyFormService;
import com.gd.upmms.service.AdmPartUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/adm/approve")
public class AdmApproveController {

    @Autowired
    private AdmPartUserService admPartUserService;

    @Autowired
    private AdmApplyFormService admApplyFormService;

    @RequestMapping("/get")
    public R<List<AdmApplyForm>> get(){
        //查询当前党组织管理员所在的党组织
        LambdaQueryWrapper<AdmPartUser> admPartUserLambdaQueryWrapper=new LambdaQueryWrapper<>();
        admPartUserLambdaQueryWrapper.eq(AdmPartUser::getUserId, BaseContext.getCurrentId());
        AdmPartUser partUser = admPartUserService.getOne(admPartUserLambdaQueryWrapper);
        //查询该党组织的入党申请信息
        LambdaQueryWrapper<AdmApplyForm> admApplyFormLambdaQueryWrapper=new LambdaQueryWrapper<>();
        admApplyFormLambdaQueryWrapper.eq(AdmApplyForm::getPartId,partUser.getPartId());
        List<AdmApplyForm> list = admApplyFormService.list(admApplyFormLambdaQueryWrapper);
        return R.success(list);
    }
}
