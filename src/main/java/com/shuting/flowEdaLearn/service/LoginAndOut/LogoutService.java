package com.shuting.flowEdaLearn.service.LoginAndOut;

import com.shuting.flowEdaLearn.commons.http.Result;
import com.shuting.flowEdaLearn.entity.user.MyOauth2UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LogoutService {
    @Autowired RedisTemplate redisTemplate;

    public Result<String> logout() {
        // 思路1:清空redis信息
        String redisKey = "";
        MyOauth2UserDetails userDetails =
                (MyOauth2UserDetails)
                        SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        redisKey = userDetails.getOauthUser().getId();
        redisTemplate.delete(redisKey);
        // 思路2:清空安全上下文信息
        //        SecurityContextHolder.clearContext();
        return Result.success("logout success");
    }
}
