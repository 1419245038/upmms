package com.gd.upmms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gd.upmms.common.R;
import com.gd.upmms.entity.AdmPosition;
import com.gd.upmms.entity.AdmPositionUser;
import com.gd.upmms.service.AdmPositionService;
import com.gd.upmms.service.AdmPositionUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/welcome")
public class IndexController {

    @Autowired
    private AdmPositionUserService admPositionUserService;

    @Autowired
    private AdmPositionService admPositionService;

    @RequestMapping("/get")
    public R<List<Map<String,Object>>> get(){
        List<Map<String,Object>> result=new ArrayList<>();
        List<AdmPosition> admPositions = admPositionService.list();
        for (AdmPosition admPosition : admPositions) {
            Map<String,Object> item=new HashMap<>();
            item.put("name",admPosition.getPositionTitle());
            LambdaQueryWrapper<AdmPositionUser> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(AdmPositionUser::getPositionId,admPosition.getPositionId());
            int count = admPositionUserService.count(queryWrapper);
            item.put("count",count);
            result.add(item);
        }
        return R.success(result);
    }
}
