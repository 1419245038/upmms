package com.gd.upmms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gd.upmms.common.BaseContext;
import com.gd.upmms.common.CustomException;
import com.gd.upmms.common.R;
import com.gd.upmms.dto.AdmApplyFormDto;
import com.gd.upmms.entity.*;
import com.gd.upmms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/adm/approve")
public class AdmApproveController {

    @Autowired
    private AdmPartUserService admPartUserService;

    @Autowired
    private AdmApplyFormService admApplyFormService;

    @Autowired
    private AdmProcessFlowService admProcessFlowService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private AdmNoticeService admNoticeService;

    @Autowired
    private AdmPositionUserService admPositionUserService;

    @RequestMapping("/get")
    public R<List<AdmApplyForm>> get(String orderNo){
        //查询当前党组织管理员所在的党组织
        LambdaQueryWrapper<AdmPartUser> admPartUserLambdaQueryWrapper=new LambdaQueryWrapper<>();
        admPartUserLambdaQueryWrapper.eq(AdmPartUser::getUserId, BaseContext.getCurrentId());
        AdmPartUser partUser = admPartUserService.getOne(admPartUserLambdaQueryWrapper);
        //查询该党组织的入党申请信息
        LambdaQueryWrapper<AdmProcessFlow> admProcessFlowLambdaQueryWrapper=new LambdaQueryWrapper<>();
        admProcessFlowLambdaQueryWrapper.eq(AdmProcessFlow::getState,"process");
        admProcessFlowLambdaQueryWrapper.eq(AdmProcessFlow::getPartId,partUser.getPartId());
        admProcessFlowLambdaQueryWrapper.eq(AdmProcessFlow::getOrderNo,orderNo);
        List<AdmProcessFlow> admProcessFlows = admProcessFlowService.list(admProcessFlowLambdaQueryWrapper);
        if (admProcessFlows.size()<1){
            throw new CustomException("无数据");
        }
        List<Integer> ids=new ArrayList<>();
        for (AdmProcessFlow admProcessFlow : admProcessFlows) {
            ids.add(admProcessFlow.getFormId());
        }
        LambdaQueryWrapper<AdmApplyForm> admApplyFormLambdaQueryWrapper=new LambdaQueryWrapper<>();
        admApplyFormLambdaQueryWrapper.in(AdmApplyForm::getFormId,ids);
        List<AdmApplyForm> list = admApplyFormService.list(admApplyFormLambdaQueryWrapper);
        return R.success(list);
    }

    @RequestMapping("/approve")
    @Transactional//开启事务
    public R<String> approve(AdmApplyFormDto applyFormDto){
        boolean flow1=false;
        boolean flow2=false;
        boolean flow3=false;
        switch (applyFormDto.getOrderNo()){
            case 2:
                //积极分子审批流程
                flow1 = processFlow1(applyFormDto);
                break;
            case 3:
                //预备党员审批流程
                flow2 = processFlow2(applyFormDto);
                break;
            case 4:
                //正式党员审批流程
                flow3 = processFlow3(applyFormDto);
                break;
            default:
                throw new CustomException("没有此流程!");
        }

        return (flow1 || flow2 || flow3)?R.success("保存成功!"):R.error("保存失败");
    }

    private SysUser getCurrentUser(){
        return sysUserService.getById(BaseContext.getCurrentId());
    }

    /**
     * 积极分子审批流程
     */
    private boolean processFlow1(AdmApplyFormDto applyFormDto) {

        boolean flag=false; //成功标识

        if ("refused".equals(applyFormDto.getResult())) {
            //当前流程修改
            LambdaUpdateWrapper<AdmProcessFlow> flow1UpdateWrapper = new LambdaUpdateWrapper<>();
            // 修改流程字段
            flow1UpdateWrapper.set(AdmProcessFlow::getResult,"refused"); //设置审批结果为驳回
            flow1UpdateWrapper.set(AdmProcessFlow::getOperatorId,BaseContext.getCurrentId()); //设置经办人编号
            flow1UpdateWrapper.set(AdmProcessFlow::getUsername,getCurrentUser().getUsername()); //设置经办人姓名
            flow1UpdateWrapper.set(AdmProcessFlow::getAuditTime,new Date()); //设置审批时间为当前时间
            flow1UpdateWrapper.set(AdmProcessFlow::getReason,applyFormDto.getReason()); //设置审批意见
            flow1UpdateWrapper.set(AdmProcessFlow::getState,"complete"); //设置此流程状态为完成
            // 修改条件
            flow1UpdateWrapper.eq(AdmProcessFlow::getOrderNo,2);
            flow1UpdateWrapper.eq(AdmProcessFlow::getFormId,applyFormDto.getFormId());
            boolean update_flow1 = admProcessFlowService.update(flow1UpdateWrapper);
            //发送通知
            //给用户的通知
            AdmNotice noticeForUser=new AdmNotice();
            noticeForUser.setReceiverId(applyFormDto.getUserId()); //通知接收人
            noticeForUser.setCreateTime(new Date()); //通知时间
            noticeForUser.setContent("您的入党申请已审批，审批结果:[驳回].审批人:["+getCurrentUser().getUsername()+"]，" +
                    "审批意见:["+applyFormDto.getReason()+"]。");
            boolean save_noticeForUser = admNoticeService.save(noticeForUser);
            //给党组织的通知
            AdmNotice noticeForPart=new AdmNotice();
            noticeForPart.setPartId(applyFormDto.getPartId()); //通知接收党组织
            noticeForPart.setCreateTime(new Date()); //通知时间
            noticeForPart.setContent("您已审批["+applyFormDto.getUsername()+"]的入党申请，审批结果:[驳回]，" +
                    "审批意见:["+applyFormDto.getReason()+"]。");
            boolean save_noticeForPart = admNoticeService.save(noticeForPart);
            //修改后续审批流程

            //取消预备党员审批流程
            LambdaUpdateWrapper<AdmProcessFlow> flow2UpdateWrapper = new LambdaUpdateWrapper<>();
            flow2UpdateWrapper.set(AdmProcessFlow::getState,"cancel");
            flow2UpdateWrapper.eq(AdmProcessFlow::getOrderNo,3);
            flow2UpdateWrapper.eq(AdmProcessFlow::getFormId,applyFormDto.getFormId());
            boolean update_flow2 = admProcessFlowService.update(flow2UpdateWrapper);

            //取消正式党员审批流程
            LambdaUpdateWrapper<AdmProcessFlow> flow3UpdateWrapper = new LambdaUpdateWrapper<>();
            flow3UpdateWrapper.set(AdmProcessFlow::getState,"cancel");
            flow3UpdateWrapper.eq(AdmProcessFlow::getOrderNo,4);
            flow3UpdateWrapper.eq(AdmProcessFlow::getFormId,applyFormDto.getFormId());
            boolean update_flow3 = admProcessFlowService.update(flow3UpdateWrapper);

            //设置申请表单状态为驳回
            LambdaUpdateWrapper<AdmApplyForm> applyFormLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
            applyFormLambdaUpdateWrapper.set(AdmApplyForm::getState,"refused");
            applyFormLambdaUpdateWrapper.eq(AdmApplyForm::getFormId,applyFormDto.getFormId());
            boolean update_applyForm = admApplyFormService.update(applyFormLambdaUpdateWrapper);

            flag = (update_flow1 || save_noticeForUser || save_noticeForPart || update_flow2 || update_flow3 || update_applyForm);
        } else {
            //当前流程修改
            LambdaUpdateWrapper<AdmProcessFlow> flow1UpdateWrapper = new LambdaUpdateWrapper<>();
            // 修改流程字段
            flow1UpdateWrapper.set(AdmProcessFlow::getResult,"approved"); //设置审批结果为同意
            flow1UpdateWrapper.set(AdmProcessFlow::getOperatorId,BaseContext.getCurrentId()); //设置经办人编号
            flow1UpdateWrapper.set(AdmProcessFlow::getUsername,getCurrentUser().getUsername()); //设置经办人姓名
            flow1UpdateWrapper.set(AdmProcessFlow::getAuditTime,new Date()); //设置审批时间为当前时间
            flow1UpdateWrapper.set(AdmProcessFlow::getReason,applyFormDto.getReason()); //设置审批意见
            flow1UpdateWrapper.set(AdmProcessFlow::getState,"complete"); //设置此流程状态为完成
            // 修改条件
            flow1UpdateWrapper.eq(AdmProcessFlow::getOrderNo,2);
            flow1UpdateWrapper.eq(AdmProcessFlow::getFormId,applyFormDto.getFormId());
            boolean update_flow1 = admProcessFlowService.update(flow1UpdateWrapper);
            //发送通知
            //给用户的通知
            AdmNotice noticeForUser=new AdmNotice();
            noticeForUser.setReceiverId(applyFormDto.getUserId()); //通知接收人
            noticeForUser.setCreateTime(new Date()); //通知时间
            noticeForUser.setContent("您的入党申请已审批，审批结果:[同意].审批人:["+getCurrentUser().getUsername()+"]，" +
                    "审批意见:["+applyFormDto.getReason()+"]，您已被发展为入党积极分子。");
            boolean save_noticeForUser = admNoticeService.save(noticeForUser);
            //给党组织的通知
            AdmNotice noticeForPart=new AdmNotice();
            noticeForPart.setPartId(applyFormDto.getPartId()); //通知接收党组织
            noticeForPart.setCreateTime(new Date()); //通知时间
            noticeForPart.setContent("您已审批["+applyFormDto.getUsername()+"]的入党申请，审批结果:[同意]，" +
                    "审批意见:["+applyFormDto.getReason()+"]，["+applyFormDto.getUsername()+"]已发展成为入党积极分子。");
            boolean save_noticeForPart = admNoticeService.save(noticeForPart);
            //修改后续审批流程

            //开启预备党员审批流程
            LambdaUpdateWrapper<AdmProcessFlow> flow2UpdateWrapper = new LambdaUpdateWrapper<>();
            flow2UpdateWrapper.set(AdmProcessFlow::getState,"process");
            flow2UpdateWrapper.eq(AdmProcessFlow::getOrderNo,3);
            flow2UpdateWrapper.eq(AdmProcessFlow::getFormId,applyFormDto.getFormId());
            boolean update_flow2 = admProcessFlowService.update(flow2UpdateWrapper);

            //将用户职务设置为入党积极分子
            AdmPositionUser positionUser=new AdmPositionUser();
            positionUser.setUserId(applyFormDto.getUserId());
            positionUser.setPositionId(3);
            boolean save_positionUser = admPositionUserService.save(positionUser);

            flag = (update_flow1 || save_noticeForUser || save_noticeForPart || update_flow2 || save_positionUser);
        }
        return flag;
    }

    /**
     * 预备党员审批流程
     */
    private boolean processFlow2(AdmApplyFormDto applyFormDto) {

        boolean flag=false; //成功标识

        if ("refused".equals(applyFormDto.getResult())) {
            //当前流程修改
            LambdaUpdateWrapper<AdmProcessFlow> flow1UpdateWrapper = new LambdaUpdateWrapper<>();
            // 修改流程字段
            flow1UpdateWrapper.set(AdmProcessFlow::getResult,"refused"); //设置审批结果为驳回
            flow1UpdateWrapper.set(AdmProcessFlow::getOperatorId,BaseContext.getCurrentId()); //设置经办人编号
            flow1UpdateWrapper.set(AdmProcessFlow::getUsername,getCurrentUser().getUsername()); //设置经办人姓名
            flow1UpdateWrapper.set(AdmProcessFlow::getAuditTime,new Date()); //设置审批时间为当前时间
            flow1UpdateWrapper.set(AdmProcessFlow::getReason,applyFormDto.getReason()); //设置审批意见
            flow1UpdateWrapper.set(AdmProcessFlow::getState,"complete"); //设置此流程状态为完成
            // 修改条件
            flow1UpdateWrapper.eq(AdmProcessFlow::getOrderNo,3);
            flow1UpdateWrapper.eq(AdmProcessFlow::getFormId,applyFormDto.getFormId());
            boolean update_flow1 = admProcessFlowService.update(flow1UpdateWrapper);
            //发送通知
            //给用户的通知
            AdmNotice noticeForUser=new AdmNotice();
            noticeForUser.setReceiverId(applyFormDto.getUserId()); //通知接收人
            noticeForUser.setCreateTime(new Date()); //通知时间
            noticeForUser.setContent("管理员已发起您的预备党员审批流程，审批结果:[驳回].审批人:["+getCurrentUser().getUsername()+"]，" +
                    "审批意见:["+applyFormDto.getReason()+"]。");
            boolean save_noticeForUser = admNoticeService.save(noticeForUser);
            //给党组织的通知
            AdmNotice noticeForPart=new AdmNotice();
            noticeForPart.setPartId(applyFormDto.getPartId()); //通知接收党组织
            noticeForPart.setCreateTime(new Date()); //通知时间
            noticeForPart.setContent("您已发起["+applyFormDto.getUsername()+"]的预备党员审批流程，审批结果:[驳回]，" +
                    "审批意见:["+applyFormDto.getReason()+"]。");
            boolean save_noticeForPart = admNoticeService.save(noticeForPart);
            //修改后续审批流程

            //取消正式党员审批流程
            LambdaUpdateWrapper<AdmProcessFlow> flow3UpdateWrapper = new LambdaUpdateWrapper<>();
            flow3UpdateWrapper.set(AdmProcessFlow::getState,"cancel");
            flow3UpdateWrapper.eq(AdmProcessFlow::getOrderNo,4);
            flow3UpdateWrapper.eq(AdmProcessFlow::getFormId,applyFormDto.getFormId());
            boolean update_flow3 = admProcessFlowService.update(flow3UpdateWrapper);

            //设置申请表单状态为驳回
            LambdaUpdateWrapper<AdmApplyForm> applyFormLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
            applyFormLambdaUpdateWrapper.set(AdmApplyForm::getState,"refused");
            applyFormLambdaUpdateWrapper.eq(AdmApplyForm::getFormId,applyFormDto.getFormId());
            boolean update_applyForm = admApplyFormService.update(applyFormLambdaUpdateWrapper);

            //删除用户的入党积极分子职务
            LambdaQueryWrapper<AdmPositionUser> positionUserLambdaQueryWrapper=new LambdaQueryWrapper<>();
            positionUserLambdaQueryWrapper.eq(AdmPositionUser::getUserId,applyFormDto.getUserId());
            boolean remove_PositionUser = admPositionUserService.remove(positionUserLambdaQueryWrapper);

            flag = (update_flow1 || save_noticeForUser || save_noticeForPart || update_flow3 || update_applyForm || remove_PositionUser);
        } else {
            //当前流程修改
            LambdaUpdateWrapper<AdmProcessFlow> flow1UpdateWrapper = new LambdaUpdateWrapper<>();
            // 修改流程字段
            flow1UpdateWrapper.set(AdmProcessFlow::getResult,"approved"); //设置审批结果为同意
            flow1UpdateWrapper.set(AdmProcessFlow::getOperatorId,BaseContext.getCurrentId()); //设置经办人编号
            flow1UpdateWrapper.set(AdmProcessFlow::getUsername,getCurrentUser().getUsername()); //设置经办人姓名
            flow1UpdateWrapper.set(AdmProcessFlow::getAuditTime,new Date()); //设置审批时间为当前时间
            flow1UpdateWrapper.set(AdmProcessFlow::getReason,applyFormDto.getReason()); //设置审批意见
            flow1UpdateWrapper.set(AdmProcessFlow::getState,"complete"); //设置此流程状态为完成
            // 修改条件
            flow1UpdateWrapper.eq(AdmProcessFlow::getOrderNo,3);
            flow1UpdateWrapper.eq(AdmProcessFlow::getFormId,applyFormDto.getFormId());
            boolean update_flow1 = admProcessFlowService.update(flow1UpdateWrapper);
            //发送通知
            //给用户的通知
            AdmNotice noticeForUser=new AdmNotice();
            noticeForUser.setReceiverId(applyFormDto.getUserId()); //通知接收人
            noticeForUser.setCreateTime(new Date()); //通知时间
            noticeForUser.setContent("管理员已发起您的预备党员审批流程，审批结果:[同意].审批人:["+getCurrentUser().getUsername()+"]，" +
                    "审批意见:["+applyFormDto.getReason()+"]，您已被发展为预备党员。");
            boolean save_noticeForUser = admNoticeService.save(noticeForUser);
            //给党组织的通知
            AdmNotice noticeForPart=new AdmNotice();
            noticeForPart.setPartId(applyFormDto.getPartId()); //通知接收党组织
            noticeForPart.setCreateTime(new Date()); //通知时间
            noticeForPart.setContent("您已发起["+applyFormDto.getUsername()+"]的预备党员审批流程，审批结果:[同意]，" +
                    "审批意见:["+applyFormDto.getReason()+"]，["+applyFormDto.getUsername()+"]已发展成为入预备党员。");
            boolean save_noticeForPart = admNoticeService.save(noticeForPart);
            //修改后续审批流程

            //开启正式党员审批流程
            LambdaUpdateWrapper<AdmProcessFlow> flow2UpdateWrapper = new LambdaUpdateWrapper<>();
            flow2UpdateWrapper.set(AdmProcessFlow::getState,"process");
            flow2UpdateWrapper.eq(AdmProcessFlow::getOrderNo,4);
            flow2UpdateWrapper.eq(AdmProcessFlow::getFormId,applyFormDto.getFormId());
            boolean update_flow2 = admProcessFlowService.update(flow2UpdateWrapper);

            //将用户职务设置为预备党员
            LambdaUpdateWrapper<AdmPositionUser> positionUserLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
            positionUserLambdaUpdateWrapper.set(AdmPositionUser::getPositionId,2);
            positionUserLambdaUpdateWrapper.eq(AdmPositionUser::getUserId,applyFormDto.getUserId());
            boolean update_positionUser = admPositionUserService.update(positionUserLambdaUpdateWrapper);

            //为用户设置党支部
            AdmPartUser partUser=new AdmPartUser();
            partUser.setUserId(applyFormDto.getUserId());
            partUser.setPartId(applyFormDto.getPartId());
            boolean save_partUser = admPartUserService.save(partUser);

            flag = (update_flow1 || save_noticeForUser || save_noticeForPart || update_flow2 || update_positionUser || save_partUser);
        }
        return flag;
    }

    /**
     * 正式党员审批流程
     */
    private boolean processFlow3(AdmApplyFormDto applyFormDto) {
        boolean flag=false; //成功标识

        if ("refused".equals(applyFormDto.getResult())) {
            //当前流程修改
            LambdaUpdateWrapper<AdmProcessFlow> flow1UpdateWrapper = new LambdaUpdateWrapper<>();
            // 修改流程字段
            flow1UpdateWrapper.set(AdmProcessFlow::getResult,"refused"); //设置审批结果为驳回
            flow1UpdateWrapper.set(AdmProcessFlow::getOperatorId,BaseContext.getCurrentId()); //设置经办人编号
            flow1UpdateWrapper.set(AdmProcessFlow::getUsername,getCurrentUser().getUsername()); //设置经办人姓名
            flow1UpdateWrapper.set(AdmProcessFlow::getAuditTime,new Date()); //设置审批时间为当前时间
            flow1UpdateWrapper.set(AdmProcessFlow::getReason,applyFormDto.getReason()); //设置审批意见
            flow1UpdateWrapper.set(AdmProcessFlow::getState,"complete"); //设置此流程状态为完成
            // 修改条件
            flow1UpdateWrapper.eq(AdmProcessFlow::getOrderNo,4);
            flow1UpdateWrapper.eq(AdmProcessFlow::getFormId,applyFormDto.getFormId());
            boolean update_flow1 = admProcessFlowService.update(flow1UpdateWrapper);
            //发送通知
            //给用户的通知
            AdmNotice noticeForUser=new AdmNotice();
            noticeForUser.setReceiverId(applyFormDto.getUserId()); //通知接收人
            noticeForUser.setCreateTime(new Date()); //通知时间
            noticeForUser.setContent("管理员已发起您的正式党员审批流程，审批结果:[驳回].审批人:["+getCurrentUser().getUsername()+"]，" +
                    "审批意见:["+applyFormDto.getReason()+"]。");
            boolean save_noticeForUser = admNoticeService.save(noticeForUser);
            //给党组织的通知
            AdmNotice noticeForPart=new AdmNotice();
            noticeForPart.setPartId(applyFormDto.getPartId()); //通知接收党组织
            noticeForPart.setCreateTime(new Date()); //通知时间
            noticeForPart.setContent("您已发起["+applyFormDto.getUsername()+"]的正式党员审批流程，审批结果:[驳回]，" +
                    "审批意见:["+applyFormDto.getReason()+"]。");
            boolean save_noticeForPart = admNoticeService.save(noticeForPart);
            //修改后续审批流程

            //设置申请表单状态为驳回
            LambdaUpdateWrapper<AdmApplyForm> applyFormLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
            applyFormLambdaUpdateWrapper.set(AdmApplyForm::getState,"refused");
            applyFormLambdaUpdateWrapper.eq(AdmApplyForm::getFormId,applyFormDto.getFormId());
            boolean update_applyForm = admApplyFormService.update(applyFormLambdaUpdateWrapper);

            //删除用户的预备党员职务
            LambdaQueryWrapper<AdmPositionUser> positionUserLambdaQueryWrapper=new LambdaQueryWrapper<>();
            positionUserLambdaQueryWrapper.eq(AdmPositionUser::getUserId,applyFormDto.getUserId());
            boolean remove_PositionUser = admPositionUserService.remove(positionUserLambdaQueryWrapper);

            //删除用户的党组织关系
            LambdaQueryWrapper<AdmPartUser> partUserLambdaQueryWrapper=new LambdaQueryWrapper<>();
            partUserLambdaQueryWrapper.eq(AdmPartUser::getUserId,applyFormDto.getUserId());
            boolean remove_partUser = admPartUserService.remove(partUserLambdaQueryWrapper);

            flag = (update_flow1 || save_noticeForUser || save_noticeForPart || update_applyForm || remove_PositionUser || remove_partUser);
        } else {
            //当前流程修改
            LambdaUpdateWrapper<AdmProcessFlow> flow1UpdateWrapper = new LambdaUpdateWrapper<>();
            // 修改流程字段
            flow1UpdateWrapper.set(AdmProcessFlow::getResult,"approved"); //设置审批结果为同意
            flow1UpdateWrapper.set(AdmProcessFlow::getOperatorId,BaseContext.getCurrentId()); //设置经办人编号
            flow1UpdateWrapper.set(AdmProcessFlow::getUsername,getCurrentUser().getUsername()); //设置经办人姓名
            flow1UpdateWrapper.set(AdmProcessFlow::getAuditTime,new Date()); //设置审批时间为当前时间
            flow1UpdateWrapper.set(AdmProcessFlow::getReason,applyFormDto.getReason()); //设置审批意见
            flow1UpdateWrapper.set(AdmProcessFlow::getState,"complete"); //设置此流程状态为完成
            // 修改条件
            flow1UpdateWrapper.eq(AdmProcessFlow::getOrderNo,4);
            flow1UpdateWrapper.eq(AdmProcessFlow::getFormId,applyFormDto.getFormId());
            boolean update_flow1 = admProcessFlowService.update(flow1UpdateWrapper);
            //发送通知
            //给用户的通知
            AdmNotice noticeForUser=new AdmNotice();
            noticeForUser.setReceiverId(applyFormDto.getUserId()); //通知接收人
            noticeForUser.setCreateTime(new Date()); //通知时间
            noticeForUser.setContent("管理员已发起您的正式党员审批流程，审批结果:[同意].审批人:["+getCurrentUser().getUsername()+"]，" +
                    "审批意见:["+applyFormDto.getReason()+"]，您已被发展为正式党员。");
            boolean save_noticeForUser = admNoticeService.save(noticeForUser);
            //给党组织的通知
            AdmNotice noticeForPart=new AdmNotice();
            noticeForPart.setPartId(applyFormDto.getPartId()); //通知接收党组织
            noticeForPart.setCreateTime(new Date()); //通知时间
            noticeForPart.setContent("您已发起["+applyFormDto.getUsername()+"]的预备党员审批流程，审批结果:[同意]，" +
                    "审批意见:["+applyFormDto.getReason()+"]，["+applyFormDto.getUsername()+"]已发展成为入正式党员。");
            boolean save_noticeForPart = admNoticeService.save(noticeForPart);
            //修改后续审批流程

            //将用户职务设置为正式党员
            LambdaUpdateWrapper<AdmPositionUser> positionUserLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
            positionUserLambdaUpdateWrapper.set(AdmPositionUser::getPositionId,1);
            positionUserLambdaUpdateWrapper.eq(AdmPositionUser::getUserId,applyFormDto.getUserId());
            boolean update_positionUser = admPositionUserService.update(positionUserLambdaUpdateWrapper);

            flag = (update_flow1 || save_noticeForUser || save_noticeForPart || update_positionUser);
        }
        return flag;
    }
}
