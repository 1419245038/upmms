package com.gd.upmms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gd.upmms.common.BaseContext;
import com.gd.upmms.common.CustomException;
import com.gd.upmms.common.R;
import com.gd.upmms.entity.*;
import com.gd.upmms.service.*;
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

    @Autowired
    private AdmProcessFlowService admProcessFlowService;

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
        //创建审批流程
        //创建用户发起的入党申请流程
        AdmProcessFlow processFlow1=new AdmProcessFlow();
        processFlow1.setFormId(admApplyForm.getFormId()); //流程所对应的申请表单编号
        processFlow1.setOperatorId(BaseContext.getCurrentId()); //设置经办人编号，次流程是由当前用户发起的申请流程，经办人编号为当前用户编号
        processFlow1.setUsername(currentUser.getUsername()); //设置经办人姓名，次流程是由当前用户发起的申请流程，经办人编号为当前用户姓名
        processFlow1.setAction("apply"); //当前流程为用户发起的申请流程
        processFlow1.setCreateTime(new Date()); //流程创建时间为当前时间
        processFlow1.setOrderNo(1); //审批任务中的第一个流程
        processFlow1.setState("complete"); //流程状态为已完成
        processFlow1.setIsLast("0"); //当前流程不是最后流程
        boolean save_flow1 = admProcessFlowService.save(processFlow1);
        //创建党组织管理员审批入党积极分子的审批流程
        AdmProcessFlow processFlow2=new AdmProcessFlow();
        processFlow2.setFormId(admApplyForm.getFormId()); //流程所对应的申请表单编号
        processFlow2.setPartId(admApplyForm.getPartId()); //设置经办党组织编号，经办党组织为用户申请加入的党组织
        processFlow2.setAction("audit"); //当前流程为审批流程
        processFlow2.setCreateTime(new Date()); //流程创建时间为当前时间
        processFlow2.setOrderNo(2); //审批任务中的第二个流程
        processFlow2.setState("process"); //流程状态为正在处理
        processFlow2.setIsLast("0"); //当前流程不是最后流程
        boolean save_flow2 = admProcessFlowService.save(processFlow2);
        //创建党组织管理员审批入党积极分子转预备党员的审批流程
        AdmProcessFlow processFlow3=new AdmProcessFlow();
        processFlow3.setFormId(admApplyForm.getFormId()); //流程所对应的申请表单编号
        processFlow3.setPartId(admApplyForm.getPartId()); //设置经办党组织编号，经办党组织为用户申请加入的党组织
        processFlow3.setAction("audit"); //当前流程为审批流程
        processFlow3.setCreateTime(new Date()); //流程创建时间为当前时间
        processFlow3.setOrderNo(3); //审批任务中的第三个流程
        processFlow3.setState("ready"); //流程状态为正在处理
        processFlow3.setIsLast("0"); //当前流程不是最后流程
        boolean save_flow3 = admProcessFlowService.save(processFlow3);
        //创建党组织管理员审批预备党员转正的审批流程
        AdmProcessFlow processFlow4=new AdmProcessFlow();
        processFlow4.setFormId(admApplyForm.getFormId()); //流程所对应的申请表单编号
        processFlow4.setPartId(admApplyForm.getPartId()); //设置经办党组织编号，经办党组织为用户申请加入的党组织
        processFlow4.setAction("audit"); //当前流程为审批流程
        processFlow4.setCreateTime(new Date()); //流程创建时间为当前时间
        processFlow4.setOrderNo(4); //审批任务中的第四个流程
        processFlow4.setState("ready"); //流程状态为正在处理
        processFlow4.setIsLast("1"); //当前流程不是最后流程
        boolean save_flow4 = admProcessFlowService.save(processFlow4);
        return (save_apply || save_noticeForUser ||
                save_noticeForPart || save_flow1 ||
                save_flow2 || save_flow3 || save_flow4)?R.success("保存成功!"):R.error("保存失败!");
    }
}
