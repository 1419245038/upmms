package com.gd.upmms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gd.upmms.service.SetmealDishService;
import com.gd.upmms.service.SetmealService;
import com.gd.upmms.entity.Setmeal;
import com.gd.upmms.entity.SetmealDish;
import com.gd.upmms.entity.SetmealDto;
import com.gd.upmms.mapper.SetmealMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author neusoft
 * @version 1.0
 * @project ruiji
 * @description
 * @date 2022/11/21 17:10:44
 */

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 保存套餐信息同时保存套餐对应的菜品信息
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐信息
        this.save(setmealDto);

        //保存菜单对应的菜品信息
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
    }
}
