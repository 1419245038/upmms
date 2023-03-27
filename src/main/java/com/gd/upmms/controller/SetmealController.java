package com.gd.upmms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gd.upmms.common.R;
import com.gd.upmms.entity.Category;
import com.gd.upmms.entity.Setmeal;
import com.gd.upmms.entity.SetmealDto;
import com.gd.upmms.service.CategoryService;
import com.gd.upmms.service.SetmealService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDish(setmealDto);
        return R.success("保存成功!");
    }

    @GetMapping("/page")
    public R<Page<SetmealDto>> page(int page,int pageSize,String name){
        Page<Setmeal> pageInfo=new Page<>(page,pageSize);
        Page<SetmealDto> dtoPage=new Page<>();

        LambdaQueryWrapper<Setmeal> qw=new LambdaQueryWrapper<>();
        qw.like(StringUtils.isNotEmpty(name),Setmeal::getName,name);
        qw.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo,qw);

        BeanUtils.copyProperties(pageInfo,dtoPage,"records");

        List<SetmealDto> list=pageInfo.getRecords().stream().map((item)->{
            SetmealDto setmealDto=new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            Category category = categoryService.getById(item.getCategoryId());
            setmealDto.setCategoryName(category.getName());
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);

        return R.success(dtoPage);
    }
}
