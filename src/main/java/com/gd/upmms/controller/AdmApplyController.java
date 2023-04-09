package com.gd.upmms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gd.upmms.common.BaseContext;
import com.gd.upmms.common.CustomException;
import com.gd.upmms.common.R;
import com.gd.upmms.entity.AdmApplyForm;
import com.gd.upmms.entity.AdmNotice;
import com.gd.upmms.entity.AdmPartUser;
import com.gd.upmms.entity.SysUser;
import com.gd.upmms.service.AdmApplyFormService;
import com.gd.upmms.service.AdmNoticeService;
import com.gd.upmms.service.AdmPartUserService;
import com.gd.upmms.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/adm")
public class AdmApplyController {


    @Autowired
    private AdmApplyFormService admApplyFormService;

    @Autowired
    private AdmNoticeService admNoticeService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private AdmPartUserService admPartUserService;

    @RequestMapping("/apply")
    @Transactional//开启事务
    public R<String> apply(AdmApplyForm admApplyForm){
        //判断用户是否已有党组织
        LambdaQueryWrapper<AdmPartUser> admPartUserLambdaQueryWrapper=new LambdaQueryWrapper<>();
        admPartUserLambdaQueryWrapper.eq(AdmPartUser::getUserId,BaseContext.getCurrentId());
        int count1 = admPartUserService.count(admPartUserLambdaQueryWrapper);
        if (count1>0){
            throw new CustomException("您已有党组织不可提交入党申请!");
        }

        //判断用户是否重复提交入党申请
        LambdaQueryWrapper<AdmApplyForm> admApplyFormLambdaQueryWrapper=new LambdaQueryWrapper<>();
        admApplyFormLambdaQueryWrapper.eq(AdmApplyForm::getUserId,BaseContext.getCurrentId()); //当前用户有入党申请记录
        admApplyFormLambdaQueryWrapper.ne(AdmApplyForm::getState,"refused"); //当前用户的入党申请未被驳回
        int count = admApplyFormService.count(admApplyFormLambdaQueryWrapper);
        if (count>0){
            throw new CustomException("不可重复提交入党申请!");
        }

        //获取当前申请人信息
        SysUser currentUser = sysUserService.getById(BaseContext.getCurrentId());
        //创建入党申请表单
        admApplyForm.setState("processing"); //设置表单状态为正在审批
        admApplyForm.setCreateTime(new Date()); //设置表单创建时间为当前时间
        admApplyForm.setUserId(BaseContext.getCurrentId()); //设置申请人id为当前登录用户的id
        boolean save_apply = admApplyFormService.save(admApplyForm); //保存入党申请信息
        //创建消息通知
        AdmNotice noticeForUser = new AdmNotice();
        noticeForUser.setCreateTime(new Date()); //设置消息通知时间为当前时间
        noticeForUser.setReceiverId(BaseContext.getCurrentId()); //设置消息接收人为当前用户
        noticeForUser.setContent("您的入党申请已提交，等待审批。");
        boolean save_noticeForUser = admNoticeService.save(noticeForUser);
        AdmNotice noticeForPart = new AdmNotice();
        noticeForPart.setCreateTime(new Date()); //设置消息通知时间为当前时间
        noticeForPart.setPartId(admApplyForm.getPartId());//设置消息接受单位为用户申请加入的党组织
        noticeForPart.setContent("用户["+currentUser.getUsername()+"],已提交入党申请，请您审批。");
        boolean save_noticeForPart = admNoticeService.save(noticeForPart);
        return (save_apply || save_noticeForUser || save_noticeForPart)?R.success("保存成功!"):R.error("保存失败!");
    }
}
