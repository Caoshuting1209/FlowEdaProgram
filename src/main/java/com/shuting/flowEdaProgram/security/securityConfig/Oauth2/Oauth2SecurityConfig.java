//package com.shuting.flowEdaLearn.commons.config.securityConfig.Oauth2;

//
//@Configuration
//@EnableMethodSecurity // 开启基于方法的授权
//public class Oauth2SecurityConfig {
//    @Autowired private DBUserDetailsManager manager;
//    @Autowired private OAuth2UserServiceImpl userService;
//    @Autowired private GrantedAuthoritiesMapper userAuthoritiesMapper;
//
//    @Bean
//    public AuthenticationManager authenticationManager() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(manager);
//        provider.setPasswordEncoder(passwordEncoder());
//        return new ProviderManager(provider);
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());
//        http.oauth2Login(
//                oauth ->
//                        oauth.userInfoEndpoint(
//                                userInfo ->
//                                        userInfo.userAuthoritiesMapper(userAuthoritiesMapper)
//                                                .userService(userService))
//                                .successHandler(new MyAuthenticationSuccessHandler()));
//        http.formLogin(
//                form ->
//                        form.successHandler(new MyAuthenticationSuccessHandler()) // 自定义登陆成功返回信息
//                                .failureHandler(
//                                        new MyAuthenticationFailureHandler())); // 自定义登陆失败返回信息);
//        http.logout(logout -> logout.logoutSuccessHandler(new MyLogoutSuccessHandler())); // 注销成功处理
//        http.exceptionHandling(
//                exception ->
//                        exception
//                                .accessDeniedHandler(new MyAccessDeniedHandler())); // 请求未授权
//        http.sessionManagement(
//                session ->
//                        session
//                                // 最多允许1个设备同时在线，后登陆的设备会把先前设备挤掉
//                                .maximumSessions(1)
//                                //账号登陆多次返回信息
//                                .expiredSessionStrategy(new MySessionInformationExpiredStrategy()));
//        // 关闭csrf攻击防御
//        http.csrf(csrf -> csrf.disable());
//        // 在全局范围内开启前后端服务器的跨域访问
//        http.cors(Customizer.withDefaults());
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(4);
//    }
//}
