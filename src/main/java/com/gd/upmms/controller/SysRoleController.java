package com.gd.upmms.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gd.upmms.common.R;
import com.gd.upmms.entity.SysRole;
import com.gd.upmms.entity.SysRoleMenu;
import com.gd.upmms.service.SysRoleMenuService;
import com.gd.upmms.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
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

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @RequestMapping("/get")
    public R<List<SysRole>> get(){
        return R.success(sysRoleService.list());
    }

    @RequestMapping("/delById")
    @Transactional//开启事务
    public R<String> delById(Integer roleId){
        boolean remove_role = sysRoleService.removeById(roleId);
        LambdaQueryWrapper<SysRoleMenu> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleMenu::getRoleId,roleId);
        boolean remove_role_menu = sysRoleMenuService.remove(queryWrapper);
        return (remove_role || remove_role_menu)?R.success("删除成功!"):R.error("删除失败!");
    }

    @RequestMapping("/delByIds")
    @Transactional//开启事务
    public R<String> delByIds(String ids){
        List<String> ids1 = new ArrayList<>(Arrays.asList(ids.split(",")));
        boolean remove_roles = sysRoleService.removeByIds(ids1);
        LambdaQueryWrapper<SysRoleMenu> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(SysRoleMenu::getRoleId,ids1);
        boolean remove_role_menus = sysRoleMenuService.remove(queryWrapper);
        return (remove_roles || remove_role_menus)?R.success("删除成功!"):R.error("删除失败!");
    }

    @RequestMapping("/add")
    @Transactional//开启事务
    public R<String> add(SysRole sysRole,String menuSelectTree_select_nodeId){
        boolean save_role = sysRoleService.save(sysRole);
        boolean save_role_menu = false;
        if(StrUtil.isNotEmpty(menuSelectTree_select_nodeId)) {
            List<SysRoleMenu> list = new ArrayList<>();
            for (String menuId : menuSelectTree_select_nodeId.split(",")) {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setRoleId(sysRole.getRoleId());
                sysRoleMenu.setMenuId(Integer.parseInt(menuId));
                list.add(sysRoleMenu);
            }
            save_role_menu = sysRoleMenuService.saveBatch(list);
        }
        return (save_role || save_role_menu)?R.success("添加成功!"):R.error("添加失败!");
    }

    @RequestMapping("/update")
    @Transactional//开启事务
    public R<String> update(SysRole sysRole,String menuSelectTree_select_nodeId){
        boolean update_role = sysRoleService.updateById(sysRole); //先更新角色信息
        boolean save_role_menu = false;
        //先删除原本的权限信息
        LambdaQueryWrapper<SysRoleMenu> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleMenu::getRoleId,sysRole.getRoleId());
        boolean remove_role_menu = sysRoleMenuService.remove(queryWrapper);
        //再插入新的权限信息
        if(StrUtil.isNotEmpty(menuSelectTree_select_nodeId)) {
            List<SysRoleMenu> list = new ArrayList<>();
            for (String menuId : menuSelectTree_select_nodeId.split(",")) {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setRoleId(sysRole.getRoleId());
                sysRoleMenu.setMenuId(Integer.parseInt(menuId));
                list.add(sysRoleMenu);
            }
            save_role_menu = sysRoleMenuService.saveBatch(list);
        }
        return (update_role || remove_role_menu || save_role_menu)?R.success("更新成功!"):R.error("更新失败!");
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
