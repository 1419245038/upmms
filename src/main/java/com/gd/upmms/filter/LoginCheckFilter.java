package com.gd.upmms.filter;

import com.alibaba.fastjson.JSON;
import com.gd.upmms.common.BaseContext;
import com.gd.upmms.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    //路径匹配器
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //获取请求的uri
        String requestURI = request.getRequestURI();
        log.info("拦截到请求:{}", requestURI);

        //定义不需要拦截的uri数组
        String[] uris = new String[]{
                "/",
                "/sys/login",
                "/sys/logout",
                "/front/**",
                "/captcha/get"
        };

        if (check(uris, requestURI)) {
            log.info("{}无需拦截", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        Integer userId = (Integer) request.getSession().getAttribute("userId");

        if (userId != null) {
            log.info("{}用户已登录，放行", requestURI);
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request, response);
            return;
        }

        log.info("{}用户未登录,拦截", requestURI);
        response.getWriter().println(JSON.toJSON(R.error("NOTLOGIN")));


    }

    private boolean check(String[] uris, String requestUri) {
        for (String s : uris) {
            boolean match = PATH_MATCHER.match(s, requestUri);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
