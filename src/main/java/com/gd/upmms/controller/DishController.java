package com.gd.upmms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gd.upmms.common.R;
import com.gd.upmms.dto.DishDto;
import com.gd.upmms.entity.Category;
import com.gd.upmms.entity.Dish;
import com.gd.upmms.service.CategoryService;
import com.gd.upmms.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author neusoft
 * @version 1.0
 * @project ruiji
 * @description
 * @date 2022/11/22 12:38:39
 */

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.saveWhitFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    @GetMapping("/page")
    public R<Page<DishDto>> page(int page,int pageSize,String name){
        Page<Dish> dishPage=new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage=new Page<>();

        LambdaQueryWrapper<Dish> qw=new LambdaQueryWrapper<>();
        qw.like(StringUtils.isNotEmpty(name),Dish::getName,name);
        qw.orderByDesc(Dish::getUpdateTime);
        dishService.page(dishPage,qw);

        BeanUtils.copyProperties(dishPage,dishDtoPage,"records");

        List<Dish> records = dishPage.getRecords();
        List<DishDto> dishDtoList = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Category category = categoryService.getById(item.getCategoryId());
            if (category != null) {
                dishDto.setCategoryName(category.getName());
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(dishDtoList);

        return R.success(dishDtoPage);
    }

    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishWhitFlavor = dishService.getByIdWhitFlavor(id);
        return R.success(dishWhitFlavor);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品成功!");
    }

    @DeleteMapping()
    public R<String> delete(String ids){
        List<Long> list=new ArrayList<>();
        for (String id : ids.split(",")) {
            list.add(Long.valueOf(id));
        }
        dishService.deleteByIdWithFlavor(list);
        return R.success("删除成功!");
    }

    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable int status,String ids){
        List<Dish> dishList=new ArrayList<>();
        for (String id : ids.split(",")) {
            Dish dish = new Dish();
            dish.setId(Long.valueOf(id));
            dish.setStatus(status);
            dishList.add(dish);
        }
        dishService.updateBatchById(dishList);
        return R.success("设置成功!");
    }

    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish){
        LambdaQueryWrapper<Dish> qw=new LambdaQueryWrapper<>();
        qw.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        qw.eq(Dish::getStatus,1);//显示起售状态为起售的菜品
        qw.orderByAsc(Dish::getSort);
        qw.orderByDesc(Dish::getUpdateTime);
        List<Dish> dishList = dishService.list(qw);
        return R.success(dishList);
    }
}
