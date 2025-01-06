package com.shuting.flowEdaLearn.commons.config.securityConfig.Oauth2.handler;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import java.io.IOException;
import java.util.HashMap;

public class MySessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event)
            throws IOException, ServletException {
        HashMap result = new HashMap();
        result.put("code", "-1");
        result.put("msg", "The account has been logged in from another device");

        // 将结果对象转化为json数据
        String json = JSON.toJSONString(result);

        // 将json数据返回给前端
        HttpServletResponse response = event.getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
    }
}
