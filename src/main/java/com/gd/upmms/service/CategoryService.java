package com.gd.upmms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gd.upmms.entity.Category;

/**
 * @author neusoft
 * @version 1.0
 * @project ruiji
 * @description
 * @date 2022/11/21 15:41:00
 */
public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
