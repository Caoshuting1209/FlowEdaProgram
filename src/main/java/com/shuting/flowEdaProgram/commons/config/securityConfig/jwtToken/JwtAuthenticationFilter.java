package com.shuting.flowEdaProgram.commons.config.securityConfig.jwtToken;


import com.shuting.flowEdaProgram.entity.user.MyOauth2UserDetails;
import com.shuting.flowEdaProgram.utils.JwtUtil;

import io.jsonwebtoken.Claims;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
 public class JwtAuthenticationFilter extends OncePerRequestFilter {
  @Autowired
  private RedisTemplate redisTemplate;

  @Override
  protected void doFilterInternal(
          HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    // 放行/login请求
    if ("/login".equals(request.getRequestURI())) {
      filterChain.doFilter(request, response);
      return;
    }
    // 获取请求头中的token
    String accessToken = request.getHeader("access_token");
    // 判断是否为空
    if (!StringUtils.hasLength(accessToken)) {
      throw new MissingRequestValueException("token is empty");
    }
    // 取得token中的用户标识，获取用户认证信息
    String subject = "";
    try {
      Claims claims = JwtUtil.parseToken(accessToken);
      subject = claims.getSubject();
    } catch (Exception e) {
      throw new RuntimeException("Wrong token");
    }
    // Redis中取出用户信息
    if(subject == null) {
        throw new MissingRequestValueException("subject is empty");
      }
    Object obj = redisTemplate.opsForValue().get(subject);
    MyOauth2UserDetails user = (MyOauth2UserDetails) obj;
    if (Objects.isNull(user)) {
      throw new RuntimeException("Redis read error");
    }
    // 将用户信息存入安全上下文
    Authentication authentication =
        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authentication);
    // 过滤放行
    filterChain.doFilter(request, response);
  }
 }
