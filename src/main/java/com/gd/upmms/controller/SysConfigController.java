package com.gd.upmms.controller;

import com.gd.upmms.common.R;
import com.gd.upmms.entity.SysConfig;
import com.gd.upmms.service.SysConfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/conf")
public class SysConfigController {

    @Autowired
    private SysConfService sysConfService;

    @RequestMapping("/get")
    public R<SysConfig> get(){
        SysConfig one = sysConfService.getOne(null);
        return R.success(one);
    }

    @RequestMapping("/update")
    public R<String> update(SysConfig sysConfig){
        boolean update = sysConfService.updateById(sysConfig);
        return update?R.success("更新成功!"):R.error("更新失败!");
    }
}
