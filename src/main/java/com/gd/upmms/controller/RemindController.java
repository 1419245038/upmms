package com.gd.upmms.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gd.upmms.common.CustomException;
import com.gd.upmms.common.R;
import com.gd.upmms.dto.SysUserDto;
import com.gd.upmms.entity.*;
import com.gd.upmms.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/remind")
public class RemindController {


    @Autowired
    private AdmPartUserService admPartUserService;

    @Autowired
    private AdmRecordService admRecordService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private AdmPositionUserService admPositionUserService;

    @Autowired
    private SysRoleUserService sysRoleUserService;

    @Autowired
    private AdmPartService admPartService;

    @Autowired
    private AdmPositionService admPositionService;

    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 获取本月未缴纳党费的用户信息
     * @return R<List<SysUserDto>>
     */
    @RequestMapping("/getUnPayUser")
    public R<List<SysUserDto>> getUnPayUser(){

        List<SysUserDto> userDtoList=new ArrayList<>();

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
            if (count<1) {
                unPayUserIds.add(partUserId);
            }
//            }else{
//                throw new CustomException("无数据");
//            }
        }
        if (unPayUserIds.isEmpty()){
            throw new CustomException("无数据");
        }
        //获取未缴费的用户信息
        LambdaQueryWrapper<SysUser> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(SysUser::getUserId,unPayUserIds);
        List<SysUser> list = sysUserService.list(queryWrapper);
        for (SysUser sysUser : list) {
            LambdaQueryWrapper<AdmPartUser> partUserLambdaQueryWrapper=new LambdaQueryWrapper<>();
            LambdaQueryWrapper<AdmPositionUser> positionUserLambdaQueryWrapper=new LambdaQueryWrapper<>();
            LambdaQueryWrapper<SysRoleUser> roleUserLambdaQueryWrapper=new LambdaQueryWrapper<>();
            partUserLambdaQueryWrapper.eq(AdmPartUser::getUserId,sysUser.getUserId());
            AdmPartUser admPartUser = admPartUserService.getOne(partUserLambdaQueryWrapper);
            positionUserLambdaQueryWrapper.eq(AdmPositionUser::getUserId,sysUser.getUserId());
            AdmPositionUser admPositionUser = admPositionUserService.getOne(positionUserLambdaQueryWrapper);
            roleUserLambdaQueryWrapper.eq(SysRoleUser::getUserId,sysUser.getUserId());
            SysRoleUser sysRoleUser = sysRoleUserService.getOne(roleUserLambdaQueryWrapper);
            SysUserDto sysUserDto=new SysUserDto();
            BeanUtils.copyProperties(sysUser,sysUserDto);
            if(admPartUser!=null) {
                AdmPart admPart = admPartService.getById(admPartUser.getPartId());
                sysUserDto.setPartTitle(admPart.getPartTitle());
                sysUserDto.setPartId(admPart.getPartId());
            }
            if(admPositionUser!=null) {
                AdmPosition admPosition = admPositionService.getById(admPositionUser.getPositionId());
                sysUserDto.setPositionTitle(admPosition.getPositionTitle());
                sysUserDto.setPositionId(admPosition.getPositionId());
            }
            if (sysRoleUser!=null){
                SysRole sysRole = sysRoleService.getById(sysRoleUser.getRoleId());
                sysUserDto.setRoleName(sysRole.getRoleName());
                sysUserDto.setRoleId(sysRole.getRoleId());
            }
            userDtoList.add(sysUserDto);
        }

        return R.success(userDtoList);
    }

    @RequestMapping("/search")
    public R<List<SysUserDto>> search(SysUserDto params){

        List<SysUserDto> userDtoList=new ArrayList<>();

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
        queryWrapper.like(StrUtil.isNotEmpty(params.getIdNumber()),SysUser::getIdNumber,params.getIdNumber());
        queryWrapper.like(StrUtil.isNotEmpty(params.getPhoneNumber()),SysUser::getPhoneNumber,params.getPhoneNumber());
        queryWrapper.like(StrUtil.isNotEmpty(params.getEmail()),SysUser::getEmail,params.getEmail());
        queryWrapper.like(StrUtil.isNotEmpty(params.getUsername()),SysUser::getUsername,params.getUsername());
        queryWrapper.like(StrUtil.isNotEmpty(params.getAddress()),SysUser::getAddress,params.getAddress());
        List<SysUser> list = sysUserService.list(queryWrapper);
        for (SysUser sysUser : list) {
            LambdaQueryWrapper<AdmPartUser> partUserLambdaQueryWrapper=new LambdaQueryWrapper<>();
            LambdaQueryWrapper<AdmPositionUser> positionUserLambdaQueryWrapper=new LambdaQueryWrapper<>();
            LambdaQueryWrapper<SysRoleUser> roleUserLambdaQueryWrapper=new LambdaQueryWrapper<>();
            partUserLambdaQueryWrapper.eq(AdmPartUser::getUserId,sysUser.getUserId());
            AdmPartUser admPartUser = admPartUserService.getOne(partUserLambdaQueryWrapper);
            positionUserLambdaQueryWrapper.eq(AdmPositionUser::getUserId,sysUser.getUserId());
            AdmPositionUser admPositionUser = admPositionUserService.getOne(positionUserLambdaQueryWrapper);
            roleUserLambdaQueryWrapper.eq(SysRoleUser::getUserId,sysUser.getUserId());
            SysRoleUser sysRoleUser = sysRoleUserService.getOne(roleUserLambdaQueryWrapper);
            SysUserDto sysUserDto=new SysUserDto();
            BeanUtils.copyProperties(sysUser,sysUserDto);
            if(admPartUser!=null) {
                AdmPart admPart = admPartService.getById(admPartUser.getPartId());
                sysUserDto.setPartTitle(admPart.getPartTitle());
                sysUserDto.setPartId(admPart.getPartId());
            }
            if(admPositionUser!=null) {
                AdmPosition admPosition = admPositionService.getById(admPositionUser.getPositionId());
                sysUserDto.setPositionTitle(admPosition.getPositionTitle());
                sysUserDto.setPositionId(admPosition.getPositionId());
            }
            if (sysRoleUser!=null){
                SysRole sysRole = sysRoleService.getById(sysRoleUser.getRoleId());
                sysUserDto.setRoleName(sysRole.getRoleName());
                sysUserDto.setRoleId(sysRole.getRoleId());
            }
            userDtoList.add(sysUserDto);
        }

        return R.success(userDtoList);
    }

    @RequestMapping("/send")
    public R<String> send(String username,String email){
        String content="["+username+"]"+"同志您好,您本月党费还未缴纳，请及时缴纳党费。";
        MailUtil.send(email, "党费缴纳提醒", content, false);
        return R.success("已发送邮件!");
    }
}
