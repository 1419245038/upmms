package com.gd.upmms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gd.upmms.dto.SysMenuDto;
import com.gd.upmms.entity.SysMenu;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {

    public List<SysMenuDto> findAll();

}
