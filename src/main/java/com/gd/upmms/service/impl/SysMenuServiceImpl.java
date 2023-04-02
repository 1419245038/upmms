package com.gd.upmms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gd.upmms.dto.SysMenuDto;
import com.gd.upmms.entity.SysMenu;
import com.gd.upmms.entity.SysRoleMenu;
import com.gd.upmms.entity.SysRoleUser;
import com.gd.upmms.mapper.SysMenuMapper;
import com.gd.upmms.service.SysMenuService;
import com.gd.upmms.service.SysRoleMenuService;
import com.gd.upmms.service.SysRoleUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {


    @Autowired
    private SysRoleUserService sysRoleUserService;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Override
    public List<SysMenuDto> findAll() {
        List<SysMenuDto> sysMenuDtos=new ArrayList<>();
        LambdaQueryWrapper<SysMenu> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(SysMenu::getSort);
        List<SysMenu> sysMenus = list(queryWrapper);
        for (SysMenu sysMenu : sysMenus) {
            SysMenuDto sysMenuDto=new SysMenuDto();
            BeanUtils.copyProperties(sysMenu,sysMenuDto);
            sysMenuDtos.add(sysMenuDto);
        }
        return sysMenuDtos;
    }

    @Override
    public List<SysMenuDto> findByUserId(Integer userId) {
        //通过userId查询用户对应的角色
        LambdaQueryWrapper<SysRoleUser> roleUserQueryWrapper=new LambdaQueryWrapper<>();
        roleUserQueryWrapper.eq(SysRoleUser::getUserId,userId);
        SysRoleUser sysRoleUser = sysRoleUserService.getOne(roleUserQueryWrapper);
        //通过用户对应的角色id查询到用户所能使用的菜单id
        LambdaQueryWrapper<SysRoleMenu> roleMenuQueryWrapper=new LambdaQueryWrapper<>();
        roleMenuQueryWrapper.eq(SysRoleMenu::getRoleId,sysRoleUser.getRoleId());
        List<SysRoleMenu> list = sysRoleMenuService.list(roleMenuQueryWrapper);
        //通过用户所能使用的菜单id查询到用户所能使用的菜单信息
        List<Integer> ids=new ArrayList<>();
        for (SysRoleMenu sysRoleMenu : list) {
            ids.add(sysRoleMenu.getMenuId());
        }
        LambdaQueryWrapper<SysMenu> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(SysMenu::getSort);
        queryWrapper.in(SysMenu::getId,ids);
        List<SysMenu> sysMenus = list(queryWrapper);
        List<SysMenuDto> sysMenuDtos=new ArrayList<>();
        for (SysMenu sysMenu : sysMenus) {
            SysMenuDto sysMenuDto=new SysMenuDto();
            BeanUtils.copyProperties(sysMenu,sysMenuDto);
            sysMenuDtos.add(sysMenuDto);
        }
        return sysMenuDtos;
    }


}
