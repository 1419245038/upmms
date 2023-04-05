package com.gd.upmms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gd.upmms.entity.AdmRecord;
import com.gd.upmms.mapper.AdmRecordMapper;
import com.gd.upmms.service.AdmRecordService;
import org.springframework.stereotype.Service;

@Service
public class AdmRecordServiceImpl extends ServiceImpl<AdmRecordMapper, AdmRecord> implements AdmRecordService {
}
