package com.gd.upmms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gd.upmms.entity.Employee;
import com.gd.upmms.mapper.EmployeeMapper;
import com.gd.upmms.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
