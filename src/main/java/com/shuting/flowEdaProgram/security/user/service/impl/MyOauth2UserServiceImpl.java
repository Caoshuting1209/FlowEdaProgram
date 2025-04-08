package com.shuting.flowEdaProgram.security.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shuting.flowEdaProgram.security.securityConfig.DBUserDetailsManager;
import com.shuting.flowEdaProgram.security.user.entity.MyOauth2User;
import com.shuting.flowEdaProgram.security.user.mapper.MyOauth2UserMapper;
import com.shuting.flowEdaProgram.security.user.service.MyOauth2UserService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MyOauth2UserServiceImpl extends ServiceImpl<MyOauth2UserMapper, MyOauth2User>
        implements MyOauth2UserService {
    @Autowired private DBUserDetailsManager manager;

    public void saveUserDetails(MyOauth2User oauth2User) {
        UserDetails userDetails =
                User.withUsername(oauth2User.getUsername())
                        .password(oauth2User.getPassword())
                        .roles("USER")
                        .build();
        manager.createUser(userDetails);
    }
}
