package com.gd.upmms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gd.upmms.common.R;
import com.gd.upmms.entity.Employee;
import com.gd.upmms.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> Login(HttpServletRequest request, @RequestBody Employee employee){

        String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());

        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        if (emp==null){
            return R.error("用户不存在!");
        }

        if (!password.equals(emp.getPassword())){
            return R.error("密码错误!");
        }

        if (emp.getStatus()!=1){
            return R.error("该用户名不可用!");
        }

        request.getSession().setAttribute("employee",emp.getId());

        return R.success(emp);
    }

    @PostMapping("/logout")
    public R<String> Logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }


    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
      /**
       *
       * 使用mybatisplus插入公共列
       *
        //设置创建时间与更新时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        //获取操作人ID
        Long empId = (Long) request.getSession().getAttribute("employee");

        //设置创建人与更新人ID
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);
       **/

        //生成默认密码123456
        String defaultPassword = DigestUtils.md5DigestAsHex("123456".getBytes());

        //设置默认密码
        employee.setPassword(defaultPassword);

        employeeService.save(employee);

        return R.success("新增员工成功!");
    }

    @GetMapping("/page")
    public R<Page<Employee>> page(int page,int pageSize,String name){

        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        Page<Employee> employeePage=new Page<>(page,pageSize);

        employeeService.page(employeePage,queryWrapper);

        return R.success(employeePage);
    }

    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){

        /**
         *
         * 使用mybatisplus插入公共列

//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));

         **/

        employeeService.updateById(employee);

        return R.success("设置成功!");
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        if (employee!=null){
            return R.success(employee);
        }
        return R.error("不存在该员工!");
    }
}
