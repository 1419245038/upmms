package com.gd.upmms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gd.upmms.entity.Setmeal;
import com.gd.upmms.entity.SetmealDto;

/**
 * @author neusoft
 * @version 1.0
 * @project ruiji
 * @description
 * @date 2022/11/21 17:10:11
 */
public interface SetmealService extends IService<Setmeal> {

    //保存套餐信息同时保存套餐对应的菜品信息
    void saveWithDish(SetmealDto setmealDto);
}
