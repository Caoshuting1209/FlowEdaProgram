package com.shuting.flowEdaProgram.security.securityConfig;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shuting.flowEdaProgram.commons.exception.MissingPropertyException;
import com.shuting.flowEdaProgram.commons.exception.ResourceAlreadyExistException;
import com.shuting.flowEdaProgram.security.user.entity.MyOauth2User;
import com.shuting.flowEdaProgram.security.user.entity.MyOauth2UserDetails;
import com.shuting.flowEdaProgram.security.user.mapper.MyOauth2UserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DBUserDetailsManager implements UserDetailsManager, UserDetailsPasswordService {
    @Autowired private MyOauth2UserMapper oauth2UserMapper;
    @Lazy @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void createUser(UserDetails user) {
        if (user.getUsername() == null) {
            throw new MissingPropertyException("username");
        }
        String password = user.getPassword();
        if (password == null) {
            throw new MissingPropertyException("password");
        }
        QueryWrapper<MyOauth2User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        if (oauth2UserMapper.selectOne(queryWrapper) != null) {
            throw new ResourceAlreadyExistException("username already exists");
        }
        MyOauth2User oauth2User = new MyOauth2User();
        oauth2User.setUsername(user.getUsername());
        oauth2User.setPassword(passwordEncoder.encode(user.getPassword()));
        oauth2User.setAuthorities("ROLE_USER");
        oauth2User.setStatus(1);
        oauth2User.setId(UUID.randomUUID().toString());
        oauth2UserMapper.insert(oauth2User);
    }

    @Override
    public void updateUser(UserDetails user) {}

    @Override
    public void deleteUser(String username) {}

    @Override
    public void changePassword(String oldPassword, String newPassword) {}

    @Override
    public boolean userExists(String username) {
        return false;
    }

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<MyOauth2User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        MyOauth2User user = oauth2UserMapper.selectOne(queryWrapper);
        if (oauth2UserMapper.selectOne(queryWrapper) == null) {
            throw new UsernameNotFoundException(username);
        }
        return new MyOauth2UserDetails(user);
    }
}
