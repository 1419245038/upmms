package com.gd.upmms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gd.upmms.dto.DishDto;
import com.gd.upmms.entity.Dish;

import java.util.List;

/**
 * @author neusoft
 * @version 1.0
 * @project ruiji
 * @description
 * @date 2022/11/21 17:09:38
 */


public interface DishService extends IService<Dish> {
    //保存菜品信息，同时保存菜品口味信息
    void saveWhitFlavor(DishDto dishDto);

    //查询菜品信息，同时查询菜品口味信息
    DishDto getByIdWhitFlavor(Long id);

    //更新菜品信息，同时更新菜品口味信息
    void updateWithFlavor(DishDto dishDto);

    //删除菜品信息，同时删除菜品口味信息
    void deleteByIdWithFlavor(List<Long> ids);
}
