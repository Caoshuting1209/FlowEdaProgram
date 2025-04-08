package com.shuting.flowEdaProgram.security.securityConfig.handler;

import com.alibaba.fastjson2.JSON;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;
import java.util.HashMap;

public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        HashMap result = new HashMap();
        result.put("code", "0");
        result.put("msg", "success logout");
        String json = JSON.toJSONString(result);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
    }
}
