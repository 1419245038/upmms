package com.gd.upmms.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gd.upmms.common.R;
import com.gd.upmms.dto.SysUserDto;
import com.gd.upmms.entity.*;
import com.gd.upmms.service.*;
import com.gd.upmms.utils.Md5Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private AdmPositionUserService admPositionUserService;

    @Autowired
    private AdmPartUserService admPartUserService;

    @Autowired
    private AdmPartService admPartService;

    @Autowired
    private AdmPositionService admPositionService;

    @Autowired
    private SysRoleUserService sysRoleUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @RequestMapping("/get")
    public R<List<SysUserDto>> get(){
        List<SysUserDto> userDtoList=new ArrayList<>();
        List<SysUser> userList = sysUserService.list();
        for (SysUser sysUser : userList) {
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
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StrUtil.isNotEmpty(params.getIdNumber()),SysUser::getIdNumber,params.getIdNumber());
        lambdaQueryWrapper.like(StrUtil.isNotEmpty(params.getPhoneNumber()),SysUser::getPhoneNumber,params.getPhoneNumber());
        lambdaQueryWrapper.like(StrUtil.isNotEmpty(params.getEmail()),SysUser::getEmail,params.getEmail());
        lambdaQueryWrapper.like(StrUtil.isNotEmpty(params.getUsername()),SysUser::getUsername,params.getUsername());
        lambdaQueryWrapper.like(StrUtil.isNotEmpty(params.getAddress()),SysUser::getAddress,params.getAddress());
        List<SysUser> userList = sysUserService.list(lambdaQueryWrapper);
        for (SysUser sysUser : userList) {
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

    @RequestMapping("/delById")
    @Transactional//开启事务
    public R<String> delById(Integer userId){
        //删除用户信息
        boolean remove_user = sysUserService.removeById(userId);
        //删除用户关联的角色、职务、党组织信息
        LambdaQueryWrapper<SysRoleUser> roleUserLambdaQueryWrapper=new LambdaQueryWrapper<>();
        LambdaQueryWrapper<AdmPartUser> partUserLambdaQueryWrapper=new LambdaQueryWrapper<>();
        LambdaQueryWrapper<AdmPositionUser> positionUserLambdaQueryWrapper=new LambdaQueryWrapper<>();
        roleUserLambdaQueryWrapper.eq(SysRoleUser::getUserId,userId);
        positionUserLambdaQueryWrapper.eq(AdmPositionUser::getUserId,userId);
        partUserLambdaQueryWrapper.eq(AdmPartUser::getUserId,userId);
        boolean remove_role = sysRoleUserService.remove(roleUserLambdaQueryWrapper);
        boolean remove_partUser = admPartUserService.remove(partUserLambdaQueryWrapper);
        boolean remove_positionUser = admPositionUserService.remove(positionUserLambdaQueryWrapper);
        return (remove_user || remove_role || remove_partUser || remove_positionUser)?R.success("删除成功!"):R.error("删除失败!");
    }

    @RequestMapping("delByIds")
    @Transactional//开启事务
    public R<String> delByIds(String ids){
        List<String> ids1 = new ArrayList<>(Arrays.asList(ids.split(",")));
        //删除用户信息
        boolean remove_users = sysUserService.removeByIds(ids1);
        //删除用户关联的角色、职务、党组织信息
        LambdaQueryWrapper<SysRoleUser> roleUserLambdaQueryWrapper=new LambdaQueryWrapper<>();
        LambdaQueryWrapper<AdmPositionUser> positionUserLambdaQueryWrapper=new LambdaQueryWrapper<>();
        LambdaQueryWrapper<AdmPartUser> partUserLambdaQueryWrapper=new LambdaQueryWrapper<>();
        roleUserLambdaQueryWrapper.in(SysRoleUser::getUserId,ids1);
        positionUserLambdaQueryWrapper.in(AdmPositionUser::getUserId,ids1);
        partUserLambdaQueryWrapper.in(AdmPartUser::getUserId,ids1);
        boolean remove_roles = sysRoleUserService.remove(roleUserLambdaQueryWrapper);
        boolean remove_positions = admPositionUserService.remove(positionUserLambdaQueryWrapper);
        boolean remove_parts = admPartUserService.remove(partUserLambdaQueryWrapper);

        return (remove_users || remove_roles
                || remove_positions || remove_parts)?
                R.success("删除成功"):R.error("删除失败");
    }

    @RequestMapping("/add")
    @Transactional//开启事务
    public R<String> add(SysUserDto sysUserDto){

        boolean save_role_user = false;
        boolean save_part_user = false;
        boolean save_position_user = false;

        //生成随机盐值
        Random random=new Random();
        int salt=random.nextInt(80)+10;
        //设置固定密码
        String password="test@123";
        //密码通过加盐操作进行加密
        sysUserDto.setSalt(salt);
        sysUserDto.setPassword(Md5Utils.md5Digest(password,salt));
        //保存用户信息
        boolean save_user = sysUserService.save(sysUserDto);
        //如果用户有关联的角色信息，保存对应的角色信息
        if(sysUserDto.getRoleId()!=null){
            SysRoleUser sysRoleUser=new SysRoleUser();
            sysRoleUser.setUserId(sysUserDto.getUserId());
            sysRoleUser.setRoleId(sysUserDto.getRoleId());
            save_role_user = sysRoleUserService.save(sysRoleUser);
        }
        //如果用户有关联的党组织信息，保存对应的党组织信息
        if(sysUserDto.getPartId()!=null){
            AdmPartUser admPartUser=new AdmPartUser();
            admPartUser.setUserId(sysUserDto.getUserId());
            admPartUser.setPartId(sysUserDto.getPartId());
            save_part_user = admPartUserService.save(admPartUser);
        }
        //如果用户有关联的职位信息，保存对应的职位信息
        if(sysUserDto.getPositionId()!=null){
            AdmPositionUser admPositionUser=new AdmPositionUser();
            admPositionUser.setUserId(sysUserDto.getUserId());
            admPositionUser.setPositionId(sysUserDto.getPositionId());
            save_position_user = admPositionUserService.save(admPositionUser);
        }
        return (save_user || save_role_user || save_part_user || save_position_user)?R.success("添加成功!"):R.error("添加失败!");
    }

    @RequestMapping("/update")
    @Transactional//开启事务
    public R<String> update(SysUserDto sysUserDto){
        boolean remove_roleUser = false;
        boolean remove_partUser = false;
        boolean remove_positionUser = false;

        boolean save_roleUser = false;
        boolean save_partUser = false;
        boolean save_positionUser = false;


        //更新用户信息
        boolean update_user = sysUserService.updateById(sysUserDto);
        //更新用户所关联的角色、职务、党组织信息
        //删除用户对应的角色信息
        LambdaQueryWrapper<SysRoleUser> roleUserLambdaQueryWrapper=new LambdaQueryWrapper<>();
        roleUserLambdaQueryWrapper.eq(SysRoleUser::getUserId,sysUserDto.getUserId());
        remove_roleUser=sysRoleUserService.remove(roleUserLambdaQueryWrapper);
        //删除用户对应的职务信息
        LambdaQueryWrapper<AdmPositionUser> positionUserLambdaQueryWrapper=new LambdaQueryWrapper<>();
        positionUserLambdaQueryWrapper.eq(AdmPositionUser::getUserId,sysUserDto.getUserId());
        remove_positionUser=admPositionUserService.remove(positionUserLambdaQueryWrapper);
        //删除用户对应的党组织信息
        LambdaQueryWrapper<AdmPartUser> partUserLambdaQueryWrapper=new LambdaQueryWrapper<>();
        partUserLambdaQueryWrapper.eq(AdmPartUser::getUserId,sysUserDto.getUserId());
        remove_partUser=admPartUserService.remove(partUserLambdaQueryWrapper);
        //插入新的用户所关联的角色、职务、党组织信息
        //插入新的用户所关联的角色信息
        if(sysUserDto.getRoleId()!=null){
            SysRoleUser sysRoleUser=new SysRoleUser();
            sysRoleUser.setUserId(sysUserDto.getUserId());
            sysRoleUser.setRoleId(sysUserDto.getRoleId());
            save_roleUser=sysRoleUserService.save(sysRoleUser);
        }
        //插入新的用户所关联的党组织信息
        if(sysUserDto.getPartId()!=null){
            AdmPartUser admPartUser=new AdmPartUser();
            admPartUser.setUserId(sysUserDto.getUserId());
            admPartUser.setPartId(sysUserDto.getPartId());
            save_partUser=admPartUserService.save(admPartUser);
        }
        //插入新的用户所关联的职务信息
        if(sysUserDto.getPositionId()!=null){
            AdmPositionUser admPositionUser=new AdmPositionUser();
            admPositionUser.setUserId(sysUserDto.getUserId());
            admPositionUser.setPositionId(sysUserDto.getPositionId());
            save_positionUser=admPositionUserService.save(admPositionUser);
        }
        return (update_user ||
                remove_roleUser || remove_partUser || remove_positionUser ||
                save_roleUser || save_partUser || save_positionUser)?
                R.success("更新成功!"):R.error("更新失败!");
    }
}
