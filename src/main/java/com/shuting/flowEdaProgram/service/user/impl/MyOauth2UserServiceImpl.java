package com.shuting.flowEdaProgram.service.user.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shuting.flowEdaProgram.commons.config.securityConfig.DBUserDetailsManager;
import com.shuting.flowEdaProgram.entity.user.MyOauth2User;
import com.shuting.flowEdaProgram.mapper.user.MyOauth2UserMapper;
import com.shuting.flowEdaProgram.service.user.MyOauth2UserService;

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
