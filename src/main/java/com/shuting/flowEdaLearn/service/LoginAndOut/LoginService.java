package com.shuting.flowEdaLearn.service.LoginAndOut;

import com.shuting.flowEdaLearn.commons.http.Result;
import com.shuting.flowEdaLearn.entity.user.LoginUser;
import com.shuting.flowEdaLearn.entity.user.MyOauth2UserDetails;
import com.shuting.flowEdaLearn.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LoginService {
    @Autowired AuthenticationManager authenticationManager;
    @Autowired RedisTemplate redisTemplate;

    public Result<String> login(LoginUser loginUser) {
        String username = loginUser.getUsername();
        String password = loginUser.getPassword();
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            if (authentication.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // 生成token
                String subject =
                        ((MyOauth2UserDetails) authentication.getPrincipal())
                                .getOauthUser()
                                .getId();
                String token = JwtUtil.generateToken(subject, 1000 * 60 * 5);
                // 写入redis
                redisTemplate
                        .opsForValue()
                        .set(
                                subject,
                                authentication.getPrincipal(),
                                1000 * 60 * 5,
                                TimeUnit.MILLISECONDS);
                return Result.success(token);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.failure("unauthorized");
    }
}
