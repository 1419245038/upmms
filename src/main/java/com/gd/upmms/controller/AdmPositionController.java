package com.gd.upmms.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gd.upmms.common.R;
import com.gd.upmms.entity.AdmPosition;
import com.gd.upmms.service.AdmPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/adm/position")
public class AdmPositionController {

    @Autowired
    private AdmPositionService admPositionService;

    @RequestMapping("get")
    public R<List<AdmPosition>> get(){
        List<AdmPosition> list = admPositionService.list();
        return R.success(list);
    }

    @RequestMapping("add")
    public R<String> add(AdmPosition admPosition){
        boolean save = admPositionService.save(admPosition);
        return save?R.success("添加成功!"):R.error("添加失败!");
    }

    @RequestMapping("delById")
    public R<String> delById(Integer positionId){
        boolean remove = admPositionService.removeById(positionId);
        return remove?R.success("删除成功!"):R.error("删除失败!");
    }

    @RequestMapping("delByIds")
    public R<String> delByIds(String ids){
        List<String> ids1 = new ArrayList<>(Arrays.asList(ids.split(",")));
        boolean removeByIds = admPositionService.removeByIds(ids1);
        return removeByIds?R.success("删除成功!"):R.error("删除失败!");
    }

    @RequestMapping("/update")
    public R<String> update(AdmPosition admPosition){
        boolean update = admPositionService.updateById(admPosition);
        return update?R.success("更新成功!"):R.error("更新失败!");
    }

    @RequestMapping("/search")
    public R<List<AdmPosition>> search(AdmPosition admPosition){
        LambdaQueryWrapper<AdmPosition> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotEmpty(admPosition.getPositionTitle()),AdmPosition::getPositionTitle,admPosition.getPositionTitle());
        queryWrapper.like(StrUtil.isNotEmpty(admPosition.getPositionDesc()),AdmPosition::getPositionDesc,admPosition.getPositionDesc());
        List<AdmPosition> list = admPositionService.list(queryWrapper);
        return R.success(list);
    }
}
