package com.shuting.flowEdaLearn.commons.config.securityConfig.handler;

import com.alibaba.fastjson2.JSON;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.util.HashMap;

public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {
        HashMap result = new HashMap();
        result.put("code", "-1");
        // 获取登陆失败信息
        result.put("msg", exception.getLocalizedMessage());
        String json = JSON.toJSONString(result);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
    }
}
