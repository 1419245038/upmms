package com.gd.upmms.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gd.upmms.common.R;
import com.gd.upmms.entity.SysRole;
import com.gd.upmms.service.SysRoleService;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：zhupenghe
 * @date ：created in 2023/03/28 13:21
 * @description :
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @RequestMapping("/get")
    public R<List<SysRole>> get(){
        return R.success(sysRoleService.list());
    }

    @RequestMapping("/delById")
    public R<String> delById(Integer roleId){
        boolean remove = sysRoleService.removeById(roleId);
        return remove?R.success("删除成功!"):R.error("删除失败!");
    }

    @RequestMapping("/delByIds")
    public R<String> delByIds(String ids){
        List<String> ids1=new ArrayList<>();
        for (String id : ids.split(",")) {
            ids1.add(id);
        }
        boolean removeByIds = sysRoleService.removeByIds(ids1);
        return removeByIds?R.success("删除成功!"):R.error("删除失败!");
    }

    @RequestMapping("/add")
    public R<String> add(SysRole sysRole){
        boolean save = sysRoleService.save(sysRole);
        return save?R.success("添加成功!"):R.error("添加失败!");
    }

    @RequestMapping("/update")
    public R<String> update(SysRole sysRole){
        boolean update = sysRoleService.updateById(sysRole);
        return update?R.success("更新成功!"):R.error("更新失败!");
    }

    @RequestMapping("/search")
    public R<List<SysRole>> search(SysRole sysRole){
        LambdaQueryWrapper<SysRole> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotEmpty(sysRole.getRoleName()),SysRole::getRoleName,sysRole.getRoleName());
        queryWrapper.like(StrUtil.isNotEmpty(sysRole.getRoleDescription()),SysRole::getRoleDescription,sysRole.getRoleDescription());
        List<SysRole> list = sysRoleService.list(queryWrapper);
        return R.success(list);
    }
}
