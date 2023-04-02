package com.gd.upmms.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gd.upmms.common.CustomException;
import com.gd.upmms.common.R;
import com.gd.upmms.entity.AdmPart;
import com.gd.upmms.entity.AdmPartUser;
import com.gd.upmms.service.AdmPartService;
import com.gd.upmms.service.AdmPartUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/adm/part")
public class AdmPartController {

    @Autowired
    private AdmPartService admPartService;

    @Autowired
    private AdmPartUserService admPartUserService;

    @RequestMapping("/get")
    public R<List<AdmPart>> get(){
        List<AdmPart> list = admPartService.list();
        return R.success(list);
    }

    @RequestMapping("/add")
    public R<String> add(AdmPart admPart){
        boolean save = admPartService.save(admPart);
        return save?R.success("添加成功!"):R.error("添加失败!");
    }

    @RequestMapping("/delById")
    public R<String> del(Integer partId){
        LambdaQueryWrapper<AdmPartUser> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(AdmPartUser::getPartId,partId);
        int count = admPartUserService.count(queryWrapper);
        if (count>0){
            throw new CustomException("此党组织下存在用户,不能删除!");
        }
        boolean remove = admPartService.removeById(partId);
        return remove?R.success("删除成功!"):R.success("删除失败!");
    }

    @RequestMapping("/update")
    public R<String> update(AdmPart admPart){
        boolean update = admPartService.updateById(admPart);
        return update?R.success("更新成功!"):R.error("更新失败!");
    }

    @RequestMapping("delByIds")
    public R<String> delByIds(String ids){
        List<String> ids1 = new ArrayList<>(Arrays.asList(ids.split(",")));
        LambdaQueryWrapper<AdmPartUser> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(AdmPartUser::getPartId,ids1);
        int count = admPartUserService.count(queryWrapper);
        if (count>0){
            throw new CustomException("此党组织下存在用户,不能删除!");
        }
        boolean removeByIds = admPartService.removeByIds(ids1);
        return removeByIds?R.success("删除成功!"):R.error("删除失败!");
    }

    @RequestMapping("/search")
    public R<List<AdmPart>> search(AdmPart admPart){
        LambdaQueryWrapper<AdmPart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotEmpty(admPart.getPartTitle()),AdmPart::getPartTitle,admPart.getPartTitle());
        queryWrapper.like(StrUtil.isNotEmpty(admPart.getPartAddr()),AdmPart::getPartAddr,admPart.getPartAddr());
        queryWrapper.like(StrUtil.isNotEmpty(admPart.getPartDesc()),AdmPart::getPartDesc,admPart.getPartDesc());
        List<AdmPart> list = admPartService.list(queryWrapper);
        return R.success(list);
    }
}
