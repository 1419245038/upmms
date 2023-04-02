package com.gd.upmms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gd.upmms.entity.SysRoleUser;
import com.gd.upmms.mapper.SysRoleUserMapper;
import com.gd.upmms.service.SysRoleUserService;
import org.springframework.stereotype.Service;

@Service
public class SysRoleUserServiceImpl extends ServiceImpl<SysRoleUserMapper,SysRoleUser> implements SysRoleUserService {
}
