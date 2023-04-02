package com.gd.upmms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gd.upmms.entity.AdmPartUser;
import com.gd.upmms.mapper.AdmPartUserMapper;
import com.gd.upmms.service.AdmPartUserService;
import org.springframework.stereotype.Service;

@Service
public class AdmPartUserServiceImpl extends ServiceImpl<AdmPartUserMapper, AdmPartUser> implements AdmPartUserService {
}
