package com.gd.upmms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gd.upmms.entity.SysConfig;
import com.gd.upmms.mapper.SysConfigMapper;
import com.gd.upmms.service.SysConfService;
import org.springframework.stereotype.Service;

@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfService {
}
