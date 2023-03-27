package com.gd.upmms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gd.upmms.common.CustomException;
import com.gd.upmms.dto.SysMenuDto;
import com.gd.upmms.entity.SysMenu;
import com.gd.upmms.mapper.SysMenuMapper;
import com.gd.upmms.service.SysMenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenuDto> findAll() {
        List<SysMenuDto> sysMenuDtos=new ArrayList<>();
        List<SysMenu> sysMenus = sysMenuMapper.selectList(null);
        for (SysMenu sysMenu : sysMenus) {
            SysMenuDto sysMenuDto=new SysMenuDto();
            BeanUtils.copyProperties(sysMenu,sysMenuDto);
            sysMenuDtos.add(sysMenuDto);
        }
        return sysMenuDtos;
    }

}
