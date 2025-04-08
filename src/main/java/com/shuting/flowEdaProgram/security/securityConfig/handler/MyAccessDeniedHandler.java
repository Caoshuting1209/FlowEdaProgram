package com.shuting.flowEdaProgram.security.securityConfig.handler;

import com.alibaba.fastjson2.JSON;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.util.HashMap;

public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        HashMap result = new HashMap();
        result.put("code", "-1");
        result.put("msg", "Unauthorized request");

        // 将结果对象转化为json数据
        String json = JSON.toJSONString(result);

        // 将json数据返回给前端
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
    }
}
