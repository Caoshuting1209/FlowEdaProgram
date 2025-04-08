package com.shuting.flowEdaProgram.commons.config.securityConfig.handler;

import com.alibaba.fastjson2.JSON;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.HashMap;

public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        HashMap result = new HashMap();
        result.put("code", "0");
        result.put("msg", "success login");
        // 返回用户身份信息
        result.put("data", authentication.getPrincipal());

        // 将结果对象转化为json数据
        String json = JSON.toJSONString(result);

        // 将json数据返回给前端
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
    }
}
