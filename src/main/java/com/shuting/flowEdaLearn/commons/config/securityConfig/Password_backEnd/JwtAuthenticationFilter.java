package com.shuting.flowEdaLearn.commons.config.securityConfig.Password_backEnd; // package
                                                                        // com.shuting.flowEdaOauth.commons.config.securityConfig.backend;

// @Component
// public class JwtAuthenticationFilter extends OncePerRequestFilter {
//  @Autowired private RedisTemplate redisTemplate;
//
//  @Override
//  protected void doFilterInternal(
//      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//      throws ServletException, IOException {
//    // 放行/login请求
//    if ("/login".equals(request.getRequestURI())) {
//      filterChain.doFilter(request, response);
//    }
//    // 获取请求头中的token
//    String accessToken = request.getHeader("access_token");
//    // 判断是否为空
//    if (StringUtils.isEmpty(accessToken)) {
//      throw new MissingRequestValueException("token is empty");
//    }
//    // 取得token中的用户标识，获取用户认证信息
//    String subject = "";
//    try {
//      Claims claims = JwtUtil.parseToken(accessToken);
//      subject = claims.getSubject();
//    } catch (Exception e) {
//      throw new RuntimeException("Wrong token");
//    }
//    // Redis中取出用户信息
//    Object obj = redisTemplate.opsForValue().get(subject);
//    MyOauth2UserDetails user = (MyOauth2UserDetails) obj;
//    if (Objects.isNull(user)) {
//      throw new RuntimeException("Redis read error");
//    }
//    // 将用户信息存入安全上下文
//    Authentication authentication =
//        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
//    SecurityContextHolder.getContext().setAuthentication(authentication);
//    // 过滤放行
//    filterChain.doFilter(request, response);
//  }
// }
