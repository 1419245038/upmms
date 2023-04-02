package com.gd.upmms.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
@RequestMapping("/captcha")
public class CaptchaController {

    @RequestMapping("/get")
    public void get(HttpServletRequest request,HttpServletResponse httpServletResponse) throws IOException {
        HttpSession session = request.getSession();
        ShearCaptcha shearCaptcha = CaptchaUtil.createShearCaptcha(95, 37, 4, 4);
        session.setAttribute("captcha",shearCaptcha.getCode());
        ServletOutputStream outputStream=httpServletResponse.getOutputStream();
        shearCaptcha.write(outputStream);
        outputStream.close();
    }
}
