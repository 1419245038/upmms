package com.gd.upmms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gd.upmms.common.R;
import com.gd.upmms.entity.SysRoleMenu;
import com.gd.upmms.service.SysRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sys/roleMenu")
public class SysRoleMenuController {


    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    /**
     * 查询用户所拥有的菜单id
     * @param roleId
     * @return menuId以，分割
     */
    @RequestMapping("/get")
    public R<String> get(Integer roleId){
        StringBuilder menuIds=new StringBuilder();
        LambdaQueryWrapper<SysRoleMenu> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleMenu::getRoleId,roleId);
        List<SysRoleMenu> list = sysRoleMenuService.list(queryWrapper);
        for(int i=0;i<list.size();i++){
            if (i<list.size()-1){
                menuIds.append(list.get(i).getMenuId());
                menuIds.append(",");
            }else {
                menuIds.append(list.get(i).getMenuId());
            }
        }
        return R.success(menuIds.toString());
    }
}
