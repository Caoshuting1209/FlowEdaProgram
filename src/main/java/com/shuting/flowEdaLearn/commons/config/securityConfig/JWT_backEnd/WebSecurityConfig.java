package com.shuting.flowEdaLearn.commons.config.securityConfig.JWT_backEnd; // package
                                                                        // com.shuting.flowEdaOauth.commons.config.securityConfig.backend;

//
// @Configuration
// @EnableWebSecurity
// @EnableMethodSecurity // 开启基于方法的授权
// public class WebSecurityConfig {
//  @Autowired private JwtAuthenticationFilter jwtAuthenticationFilter;
//  @Autowired private DBUserDetailsManager manager;
//
//  @Bean
//  public AuthenticationManager authenticationManager() {
//    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//    provider.setUserDetailsService(manager);
//    provider.setPasswordEncoder(passwordEncoder());
//    return new ProviderManager(provider);
//  }
//
//  @Bean
//  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//    http.authorizeHttpRequests(
//            authorize ->
//                authorize.requestMatchers("/login").permitAll().anyRequest().authenticated())
//        .csrf(csrf -> csrf.disable())
//        .cors(Customizer.withDefaults())
//        .sessionManagement(session -> session.disable())
//        .addFilterBefore(jwtAuthenticationFilter, AuthorizationFilter.class)
//        .logout(logout -> logout.disable())
//        .formLogin(form -> form.disable());
//
//    return http.build();
//  }
//
//  @Bean
//  public PasswordEncoder passwordEncoder() {
//    return new BCryptPasswordEncoder(4);
//  }
// }
