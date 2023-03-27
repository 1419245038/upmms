package com.gd.upmms.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
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
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(96, 37);
        session.setAttribute("captcha",lineCaptcha.getCode());
        ServletOutputStream outputStream=httpServletResponse.getOutputStream();
        lineCaptcha.write(outputStream);
        outputStream.close();
    }
}
