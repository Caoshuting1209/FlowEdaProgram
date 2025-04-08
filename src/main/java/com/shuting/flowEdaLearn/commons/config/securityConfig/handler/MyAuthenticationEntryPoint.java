package com.shuting.flowEdaLearn.commons.config.securityConfig.handler;

import com.alibaba.fastjson2.JSON;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.HashMap;

public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException, ServletException {
        // 当用户访问未经认证的接口时，会自动跳转到登陆界面，此方法用于覆盖这种自动跳转
        HashMap result = new HashMap();
        result.put("code", "-1");
        result.put("msg", authException.getLocalizedMessage());
        String json = JSON.toJSONString(result);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
    }
}
