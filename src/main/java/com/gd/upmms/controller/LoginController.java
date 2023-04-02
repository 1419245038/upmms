package com.gd.upmms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gd.upmms.common.BaseContext;
import com.gd.upmms.common.CustomException;
import com.gd.upmms.common.R;
import com.gd.upmms.entity.SysUser;
import com.gd.upmms.service.SysUserService;
import com.gd.upmms.utils.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/sys")
public class LoginController {

    @Autowired
    private SysUserService sysUserService;

    @RequestMapping("/login")
    public R<Map<String,Object>> login(HttpServletRequest request,String username, String password, String captcha){
        Map<String,Object> result=new HashMap<>();
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUser::getIdNumber,username);
        SysUser user = sysUserService.getOne(lambdaQueryWrapper);
        if (user==null){
            throw new CustomException("身份证号不存在!");
        }
        String md5_password = Md5Utils.md5Digest(password, user.getSalt());
        if (!md5_password.equals(user.getPassword())){
            throw new CustomException("密码错误!");
        }
        HttpSession session = request.getSession();
        String captcha_string=(String) session.getAttribute("captcha");
        if (!captcha.equals(captcha_string)){
            throw new CustomException("验证码错误!");
        }
        result.put("username",user.getUsername());
        result.put("userId",user.getUserId());
        request.getSession().setAttribute("userId",user.getUserId());
        return R.success(result);
    }

    @RequestMapping("/logout")
    public R<String> Logout(HttpServletRequest request){
        request.getSession().removeAttribute("userId");
        return R.success("退出登录成功");
    }

    @RequestMapping("/updatePassword")
    public R<String> updatePassword(HttpServletRequest request,String oldPassword,String newPassword){
        //判断旧密码是否正确
        SysUser user = sysUserService.getById(BaseContext.getCurrentId());
        if (!Md5Utils.md5Digest(oldPassword,user.getSalt()).equals(user.getPassword())){
            throw new CustomException("旧密码不正确!");
        }
        //修改密码
        //生成随机盐值
        Random random=new Random();
        int salt=random.nextInt(80)+10;
        //构建更新条件
        UpdateWrapper<SysUser> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("user_id", BaseContext.getCurrentId());
        updateWrapper.set("salt",salt);
        updateWrapper.set("password",Md5Utils.md5Digest(newPassword,salt));
        boolean update = sysUserService.update(null,updateWrapper);
        request.getSession().removeAttribute("userId");
        return update?R.success("修改成功!"):R.error("修改失败!");
    }
}
