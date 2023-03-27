package com.gd.upmms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gd.upmms.common.CustomException;
import com.gd.upmms.entity.Category;
import com.gd.upmms.entity.Dish;
import com.gd.upmms.entity.Setmeal;
import com.gd.upmms.mapper.CategoryMapper;
import com.gd.upmms.service.CategoryService;
import com.gd.upmms.service.DishService;
import com.gd.upmms.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author neusoft
 * @version 1.0
 * @project ruiji
 * @description
 * @date 2022/11/21 15:41:47
 */

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper,Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long id){

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper=new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        if (count1>0){
            throw new CustomException("该分类下关联了菜品，不能删除");
        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper=new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2>0){
            throw new CustomException("该分类下关联了套餐，不能删除");
        }

        super.removeById(id);

    }
}
