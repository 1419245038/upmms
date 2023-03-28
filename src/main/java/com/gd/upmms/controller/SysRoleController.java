package com.gd.upmms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gd.upmms.common.R;
import com.gd.upmms.entity.SysRole;
import com.gd.upmms.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
