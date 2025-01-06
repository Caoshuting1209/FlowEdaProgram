package com.shuting.flowEdaLearn.commons.config.securityConfig.Oauth2;

import com.shuting.flowEdaLearn.commons.config.Oauth2.OAuth2UserServiceImpl;
import com.shuting.flowEdaLearn.commons.config.securityConfig.DBUserDetailsManager;
import com.shuting.flowEdaLearn.commons.config.securityConfig.Oauth2.handler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity // 开启基于方法的授权
public class Oauth2SecurityConfig {
    @Autowired private DBUserDetailsManager manager;
    @Autowired private OAuth2UserServiceImpl userService;
    @Autowired private GrantedAuthoritiesMapper userAuthoritiesMapper;

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(manager);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());
        http.oauth2Login(
                oauth ->
                        oauth.userInfoEndpoint(
                                userInfo ->
                                        userInfo.userAuthoritiesMapper(userAuthoritiesMapper)
                                                .userService(userService)));
        http.formLogin(Customizer.withDefaults());
        http.logout(logout -> logout.logoutSuccessHandler(new MyLogoutSuccessHandler())); // 注销成功处理
        http.exceptionHandling(
                exception ->
                        exception
//                                .authenticationEntryPoint(new MyAuthenticationEntryPoint())
                                .accessDeniedHandler(new MyAccessDeniedHandler())); // 请求未认证的处理
        http.sessionManagement(
                session ->
                        session
                                // 最多允许1个设备同时在线
                                .maximumSessions(1)
                                // 后登陆的设备会把先前设备挤掉
                                .expiredSessionStrategy(new MySessionInformationExpiredStrategy()));
        // 关闭csrf攻击防御
        http.csrf(csrf -> csrf.disable());
        // 在全局范围内开启前后端服务器的跨域访问
        http.cors(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }
}
