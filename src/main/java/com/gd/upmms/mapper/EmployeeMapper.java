package com.gd.upmms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gd.upmms.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
