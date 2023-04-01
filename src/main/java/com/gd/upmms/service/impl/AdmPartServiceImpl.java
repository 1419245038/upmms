package com.gd.upmms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gd.upmms.entity.AdmPart;
import com.gd.upmms.mapper.AdmPartMapper;
import com.gd.upmms.service.AdmPartService;
import org.springframework.stereotype.Service;

@Service
public class AdmPartServiceImpl extends ServiceImpl<AdmPartMapper, AdmPart> implements AdmPartService {
}
