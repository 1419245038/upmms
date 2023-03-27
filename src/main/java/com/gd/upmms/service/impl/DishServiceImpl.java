package com.gd.upmms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gd.upmms.dto.DishDto;
import com.gd.upmms.entity.Dish;
import com.gd.upmms.entity.DishFlavor;
import com.gd.upmms.mapper.DishMapper;
import com.gd.upmms.service.DishFlavorService;
import com.gd.upmms.service.DishService;
import org.springframework.beans.BeanUtils;
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
 * @date 2022/11/21 17:11:38
 */

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 保存菜品信息，同时保存菜品口味信息
     * @param dishDto
     */
    @Override
    @Transactional//开启事务
    public void saveWhitFlavor(DishDto dishDto) {
        //保存菜品信息
        this.save(dishDto);

        //菜品口味信息
        List<DishFlavor> flavors = dishDto.getFlavors();
        //通过dishId将菜品信息和菜品口味信息相关联
        flavors=flavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        //保存菜品口味信息
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 查询菜品信息，同时查询菜品口味信息
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWhitFlavor(Long id) {

        DishDto dishDto=new DishDto();

        Dish dish = this.getById(id);
        BeanUtils.copyProperties(dish,dishDto);
        LambdaQueryWrapper<DishFlavor> qw=new LambdaQueryWrapper<>();
        qw.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavorList = dishFlavorService.list(qw);
        dishDto.setFlavors(flavorList);
        return dishDto;
    }

    @Override
    @Transactional//开启事务
    public void updateWithFlavor(DishDto dishDto) {
        //更新菜品信息
        this.updateById(dishDto);

        //先删除菜品对应的口味信息
        LambdaQueryWrapper<DishFlavor> qw=new LambdaQueryWrapper<>();
        qw.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(qw);

        //在添加修改后的菜品口味信息
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    @Transactional//开启事务
    public void deleteByIdWithFlavor(List<Long> ids) {

        List<Dish> dishList = this.listByIds(ids);

        //删除菜品信息
        this.removeByIds(ids);
        //删除菜品对应的口味信息
        LambdaQueryWrapper<DishFlavor> qw=new LambdaQueryWrapper<>();
        dishList.stream().forEach((item)->{
            qw.eq(DishFlavor::getDishId,item.getId());
            dishFlavorService.remove(qw);
        });

    }
}
