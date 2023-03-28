package com.gd.upmms.controller;

import com.gd.upmms.common.R;
import com.gd.upmms.entity.SysUser;
import com.gd.upmms.service.SysUserService;
import com.gd.upmms.utils.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * @author ：zhupenghe
 * @date ：created in 2023/03/28 12:43
 * @description :
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @RequestMapping("/add")
    public R<String> add(SysUser sysUser){
        Random random=new Random();
        String password="test@123";
        int salt=random.nextInt(80)+10;
        sysUser.setSalt(salt);
        sysUser.setPassword(Md5Utils.md5Digest(password,salt));
        boolean save = sysUserService.save(sysUser);
        return save?R.success("添加成功!"):R.error("添加失败!");
    }
}
