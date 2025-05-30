package com.shuting.flowEdaProgram.security.user.service.LoginAndOut;

import com.shuting.flowEdaProgram.commons.http.Result;

import com.shuting.flowEdaProgram.security.user.entity.MyOauth2UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LogoutService {
    @Autowired private RedisTemplate redisTemplate;

    public Result<String> logout() {
        // 清空redis信息
        String redisKey = "";
        MyOauth2UserDetails userDetails =
                (MyOauth2UserDetails)
                        SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        redisKey = userDetails.getOauthUser().getId();
        redisTemplate.delete(redisKey);
        return Result.success("logout success");
    }
}
