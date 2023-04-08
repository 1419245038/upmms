package com.gd.upmms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gd.upmms.entity.AdmNotice;
import com.gd.upmms.mapper.AdmNoticeMapper;
import com.gd.upmms.service.AdmNoticeService;
import org.springframework.stereotype.Service;

@Service
public class AdmNoticeServiceImpl extends ServiceImpl<AdmNoticeMapper, AdmNotice> implements AdmNoticeService {
}
