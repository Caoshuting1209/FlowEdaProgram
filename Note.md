## Flow_eda_learn

### Part 1: 业务部分

##### 1. 新建项目，注入依赖，配置

##### 2. 定义Flow实体类和接口

##### 3. 交互数据库，在service层实现增删改查功能

##### 4. 全局异常处理

###### 4.1 建立一个统一的错误信息类

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    public static final String INVALID_PARAMETER = "Invalid parameter";
    public static final String INTERNAL_ERROR = "Internal error";
    public static final String RESOURCE_NOT_FOUND = "Resource not found";
    public static final String MISS_PROPERTY_IN_BODY = "Missing property in request body";
    public static final String INVALID_STATUS = "Invalid status";

    private String error;
    private String message;
    private Integer code;
    private String path;
}
```

###### 4.2 建立全局异常类

```java
//全局异常类
@Data
public class FlowException extends RuntimeException {
    private String error;
    private String message;
    private HttpStatus httpStatus;

    public FlowException(String error, String message) {
        super(message);
        this.error = error;
        this.message = message;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }
}
```

###### 4.3 按需求建立异常类的拓展类

```java
//分模块异常类拓展
public class InvalidParameterException extends FlowException {
    public InvalidParameterException(String message) {
        super(ApiError.INVALID_PARAMETER, message);
    }
}
```

###### 4.4 自定义异常处理器

```java
//自定义异常处理器
@ControllerAdvice
@Slf4j
public class FlowExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleFLowExceptions(Exception ex, WebRequest request) throws Exception {
        HttpHeaders headers = new HttpHeaders();
      //匹配异常类
        if (ex instanceof FlowException) {
            FlowException e = (FlowException) ex;
            return handleExceptionInternal(e, null, headers, e.getHttpStatus(), request);
        }
        return handleExceptionInternal(ex, null, headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
      	//传入的body默认为null，直接新建一个ApiError类作为body
        ApiError apiError = new ApiError();
        apiError.setError(ApiError.INTERNAL_ERROR);
        apiError.setMessage(ex.getMessage());
        apiError.setCode(statusCode.value());
        apiError.setPath(getURI(request)) ;
        body = apiError;
      //后端日志
        log.error("Catch error: {}", apiError.getMessage());
        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
      //body的内容即为前端报错信息（ApiError类，包含error，message，code，path参数）
    }

    private String getURI(WebRequest request) {
        if (request instanceof ServletWebRequest) {
            return ((ServletWebRequest) request).getRequest().getRequestURI();
        }
        return null;
    }
}
```

###### 4.5 配置异常处理器

```java
@Configuration
public class SpringWebAutoConfig implements WebMvcConfigurer {
  @Bean
  public FlowExceptionHandler flowExceptionHandler() {
    return new FlowExceptionHandler();
  }
}
```



##### 5. 完善流程节点业务

###### 5.1 节点类型（NodeType）接口定义

> 通过NodeType的id确定需要传入的params种类

###### 5.2 节点数据（NodeData）接口定义

> 当新建一条NodeData数据时，通过数据的typeId确定params的种类，作为该条NodeData（Map类）中params的keySet，从用户端接收key的value值，封装params字段值，生成新纪录（具体运行过程中运行过程中会涉及到一个GenerateNodeData的方法，这个方法的传入参数包括typeId、params中的key对应的value值，输出结果为NodeData类型），加入List<NodeData> list中。一个list数据组合成一个flow，save操作和Version更新操作的对象都是这个list

###### 5.3 节点类型参数（NodeTypeParams）接口定义



##### 6. 版本相关的一些方法更新

###### 6.1 分页方法的实现

```xml
<!--引入依赖-->
<dependency>
      <groupId>com.baomidou</groupId>
      <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
      <version>3.5.9</version>
  </dependency>
  <dependency>
      <groupId>com.baomidou</groupId>
      <artifactId>mybatis-plus-jsqlparser</artifactId>
      <version>3.5.9</version>
  </dependency>
```

```java
@Configuration
public class MybatisPlusConfig {
    @Bean
    //分页插件
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor()); 
      // 对于 Spring Boot 3，使用 PaginationInnerInterceptor
        return interceptor;
    }
}

//service层的实现方法
public IPage<Flow> listFlow(FlowRequest flowRequest) {
  //page参数指代当前页码，limit指代每页的记录数
        Page<Flow> page = new Page<>(flowRequest.getPage(), flowRequest.getLimit());
  //这里写查询条件
        QueryWrapper<Flow> queryWrapper = new QueryWrapper<>();
        if (flowRequest.getName() != null) {
            queryWrapper.eq("name", flowRequest.getName());
        }
        if (flowRequest.getStatus() != null) {
            queryWrapper.eq("status", flowRequest.getStatus());
        }
        queryWrapper.orderByAsc("create_time");
  
  //selectPage是分页插件实现的分页方法
        IPage<Flow> resPage = flowMapper.selectPage(page, queryWrapper);
        return resPage;
    }
```



###### 6.2 数据插入与更新时间的自动填充

```java
//建立需要自动填充的字段名和和insertFill、updataFill方法间的映射
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }
}

//在实体类中对相应字段进行注解
@Data
@TableName("eda_flow")
public class Flow {
    private String id;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}


```



###### 6.3 参数分组校验

- 首先建立validation package

- 在该package下建立所有的分组接口

  ```java
  //例如：
  public interface DeleteGroup {}
  
  public interface PostGroup {}
  ```

- 在对应的实体类中注解校验参数

  ```java
  //例如：
  @Data
  @TableName("eda_flow")
  public class Flow {
      @NotNull(
        //这里可以返回校验失败的具体参数信息，但必须自定义对应的exceptionHandler，否则只会返回给客户端默认的校验失败信息，而不会包含具体参数
        			message = "",
        //这个语句表示，在以下两个组中需要校验id是否为空
              groups = {DeleteGroup.class, PostGroup.class})
      private String id;
  }
  ```

- Controller层注解校验分组

  ```java
  @RestController
  public class FlowController {
      @Autowired private FlowService flowService;
      @DeleteMapping("/flow")
    //这里调用所有DeleteGroup.class的参数校验，即校验id是否为空
        public Result<Flow> deleteFlow(@Validated({DeleteGroup.class})  @RequestBody Flow flow) {
            return flowService.deleteFlow(flow);
        }
    }
  ```
  

###### 6.4 数据库json格式的数据与javaObject的交互

>  以NodeType实体类为例，其中的字段params类型为List<NodeTypeParams>，NodeTypeParams为自定义的实体类

- 引入jackson相关依赖

  ```xml
  <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-databind</artifactId>
      <version>2.18.2</version>
  </dependency>
  <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-annotations</artifactId>
      <version>2.18.2</version>
  </dependency>
  ```

- 自定义typeHandler，采用ObjectMapper的writeValueAsString()和readValue()方法来进行数据转换

  ```java
  //这是一个通用的typeHandler，其中List<NodeTypeParams>类型可以更改为其他需要的类型
  public class NodeTypeParamsTypeHandler extends BaseTypeHandler<List<NodeTypeParams>> {
      private static final ObjectMapper objectMapper = new ObjectMapper();
  
      @Override
      public void setNonNullParameter(
              PreparedStatement ps, int i, List<NodeTypeParams> parameter, JdbcType jdbcType)
              throws SQLException {
          if (parameter == null) {
              ps.setString(i, null);
          }
          if (parameter != null) {
              try {
                  ps.setString(i, objectMapper.writeValueAsString(parameter));
              } catch (JsonProcessingException e) {
                  throw new SQLException("Error converting parameter list to JSON string", e);
              }
          }
      }
  
      @Override
      public List<NodeTypeParams> getNullableResult(ResultSet rs, String columnName)
              throws SQLException {
          String json = rs.getString(columnName);
          return parseJson(json);
      }
  
      @Override
      public List<NodeTypeParams> getNullableResult(ResultSet rs, int columnIndex)
              throws SQLException {
          String json = rs.getString(columnIndex);
          return parseJson(json);
      }
  
      @Override
      public List<NodeTypeParams> getNullableResult(CallableStatement cs, int columnIndex)
              throws SQLException {
          String json = cs.getString(columnIndex);
          return parseJson(json);
      }
  
      private List<NodeTypeParams> parseJson(String json) throws SQLException {
          try {
              if (json != null && !json.isEmpty()) {
                  return objectMapper.readValue(json, new TypeReference<>() {});
              }
              return null;
          } catch (JsonProcessingException e) {
              throw new SQLException("Error parsing JSON string to parameter list", e);
          }
      }
  }
  
  ```

- 在NodeType实体类中对相应字段进行注解

  ```java
  @Data
  //注意这里必须标注autoResultMap = true
  @TableName(value = "eda_flow_node_type", autoResultMap = true)
  public class NodeType {
      private Long id;
      private String type;
      private String typeName;
      private String menu;
      private String description;
      private String background;
      private String svg;
  //这里需要标注typeHandler的类型
      @TableField(typeHandler = NodeTypeParamsTypeHandler.class)
      private List<NodeTypeParams> params;
  }
  
  ```
  
- 在对应的业务层调用数据转换方法

  ```java
  @Service
  @Slf4j
  //实现类扩展ServiceImpl<NodeTypeMapper, NodeType>，可简化数据存取过程
  public class NodeTypeService extends ServiceImpl<NodeTypeMapper, NodeType> {
      private static final List<String> MENU = Arrays.asList("基础", "运算", "解析", "网络", "数据库", "子流程");
      @Autowired private NodeTypeMapper nodeTypeMapper;
      @Autowired private NodeTypeParamsMapper nodeTypeParamsMapper;
  
      public Map<String, Object> getAllNodeTypes() {
          Map<String, Object> result = new HashMap<String, Object>();
        //这时候读取到的list已经包含了json数据转化过的字段数据
          List<NodeType> list = nodeTypeMapper.selectList(null);
        //以下语句只有NodeType表初始化时需要使用
          list.forEach(this::mergeNodeType);
          MENU.forEach(
                  k -> result.put(k, list.stream().filter(nodeType -> k.equals(nodeType.getMenu()))));
          return result;
      }
  
       //以下代码只在初始化NodeType的params时运行一次，之后的数据读取不需要用到
          private void mergeNodeType(NodeType nodeType) {
            //找到每个type下所有的TypeParams
              List<NodeTypeParams> list = nodeTypeParamsMapper.findByTypeId(nodeType.getId());
            //将这个list赋值给该nodeType数据的params字段
              nodeType.setParams(list);
            //将结果存入数据库（这里自定义的typeHandler已经在起作用了），如果主键存在，则更新，如果不存在，则存入数据
              this.saveOrUpdate(nodeType);
          }
  }
  
  
  ```
  

###### 6.5 用java方法代替手写SQL

> 当前配置中，手写SQL的方法可以在postman测试通过，但java方法代替SQL的方法会报错，报错信息：错误的SQL语句。

```java
//手写SQL的写法如下
@Repository
public interface NodeDataMapper extends BaseMapper<NodeData> {
    @Select("SELECT * FROM eda_flow_node_data WHERE flow_id = #{flowId}")
    public List<NodeData> findByFlowId(String flowId);
}

//用java方法代替写法如下
@Repository
//mapper层只需要继承增删改查功能，不需要手写SQL语句
public interface NodeDataMapper extends BaseMapper<NodeData> {}

//这里在service层用QueryWrapper封装查询条件，用BaseMapper自带的功能实现业务逻辑
private List<NodeData> findByFlowId(String flowId) {
        QueryWrapper<NodeData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("flow_id", flowId);
        return nodeDataMapper.selectList(queryWrapper);
}
 public List<NodeData> getNodeData(String flowId) {
        return findByFlowId(flowId);
}
```



### Part2: 认证与鉴权部分

##### 1. 基于纯后端的JWTtoken进行认证

###### 1.1 User表和自定义的UserDetails表

```java
@TableName(value = "oauth2_user")
@Data
public class MyOauth2User implements Serializable {

    @TableId private String username;
    private String password;
    private String authorities;
    private Integer status;
    private String id;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyOauth2UserDetails implements UserDetails {
    private MyOauth2User oauthUser;

    @Override
    public boolean isAccountNonExpired() {
        return oauthUser.getStatus() == 1;
    }

    @Override
    public boolean isAccountNonLocked() {
        return oauthUser.getStatus() == 1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return oauthUser.getStatus() == 1;
    }

    @Override
    public boolean isEnabled() {
        return oauthUser.getStatus() == 1;
    }

    @Override
  //从数据库读取用户权限
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(oauthUser.getAuthorities().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return oauthUser.getPassword();
    }

    @Override
    public String getUsername() {
        return oauthUser.getUsername();
    }
}
```



###### 1.2 构建基于数据库的信息管理器

```java
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
```



###### 1.3 JWT组件（token的生成和解析策略）

```java
public class JwtUtil {
  //随机生成十六进制字符串类型的字符串
    private static final String SECRET =
            "d11d47d81109f095dc88c519a2d838ba5744a329d7dbb337c6bf6adf818b1252";

    private static SecretKey hexStringToSecretKey() {
        // 将密钥转换为字节数组
        byte[] keyBytes = new byte[SECRET.length() / 2];
        for (int i = 0; i < SECRET.length(); i += 2) {
            keyBytes[i / 2] =
                    (byte)
                            ((Character.digit(SECRET.charAt(i), 16) << 4)
                                    + Character.digit(SECRET.charAt(i + 1), 16));
        }
        // 使用字节数组创建SecretKeySpec对象（生成token的密钥）
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    public static String generateToken(String subject, long expire) {
        String token =
                Jwts.builder()
                        .subject(subject)
                        .issuer("shuting")
                        .issuedAt(new Date())
                        .expiration(new Date(System.currentTimeMillis() + expire)) //过期时间
                        .signWith(hexStringToSecretKey()) //token签名
                        .compact();
        return token;
    }

    public static Claims parseToken(String token) {
        Jws<Claims> claims =
             Jwts.parser().verifyWith(hexStringToSecretKey()).build().parseSignedClaims(token);
        return claims.getPayload();
    }
}
```



###### 1.4 自定义的 JwtAuthenticationFilter

```java
 @Component
 public class JwtAuthenticationFilter extends OncePerRequestFilter {
  @Autowired private RedisTemplate redisTemplate;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    // 放行"/login"请求
    if ("/login".equals(request.getRequestURI())) {
      filterChain.doFilter(request, response);
    }
    // 获取请求头中的token
    String accessToken = request.getHeader("access_token");
    // 判断是否为空
    if (StringUtils.isEmpty(accessToken)) {
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
```



###### 1.5 WebSecurityConfig

```java
 @Configuration
 @EnableWebSecurity //在springboot项目中这个注解可以省略，项目会自动识别
 @EnableMethodSecurity // 开启基于方法的授权
 public class WebSecurityConfig {
  @Autowired private JwtAuthenticationFilter jwtAuthenticationFilter;
  @Autowired private DBUserDetailsManager manager;

   //引入基于数据库的信息管理器（区别于基于内存，更贴近真实应用）
  @Bean
  public AuthenticationManager authenticationManager() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(manager);
    provider.setPasswordEncoder(passwordEncoder());
    return new ProviderManager(provider);
  }

   //前后端分离，表单登陆、登出功能禁用
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            authorize ->
      //放行"/login"便于后端登陆
                authorize.requestMatchers("/login").permitAll().anyRequest().authenticated())
        .csrf(csrf -> csrf.disable())
        .cors(Customizer.withDefaults())
        .exceptionHandling(
                exception ->
                        exception
      													//自定义未认证请求返回信息（前端默认跳转到登陆页）
      													.authenticationEntryPoint(new MyAuthenticationEntryPoint())
          											//自定义未授权请求返回信息
                                .accessDeniedHandler(new MyAccessDeniedHandler()))
      //新增JwtAuthenticationFilter用于验证token信息，便于后端带token进行测试
        .addFilterBefore(jwtAuthenticationFilter, AuthorizationFilter.class)
      //禁用注销功能，用自定义的logoutService代替
        .logout(logout -> logout.disable())
      //禁用表单登陆功能，用自定义的loginService代替
        .formLogin(form -> form.disable())
      //禁用会话管理，因为后端测试不涉及账号同时登陆问题
      	.sessionManagement(session -> session.disable());
    return http.build();
  }

   //自定义的密码编辑器
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(4);
  }
 }
```



###### 1.6 自定义LoginService

```java
@Service
public class LoginService {
    @Autowired AuthenticationManager authenticationManager;
    @Autowired RedisTemplate redisTemplate;

    public Result<String> login(LoginUser loginUser) {
        String username = loginUser.getUsername();
        String password = loginUser.getPassword();
      //生成authenticationToken
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        try {
          //authenticationToken与数据库中的信息进行比对
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
          //用户名与密码成功匹配后在安全上下文中添加该认证信息
            if (authentication.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // 生成token令牌
                String subject =
                        ((MyOauth2UserDetails) authentication.getPrincipal())
                                .getOauthUser()
                                .getId(); //以id作为subject，避免使用username暴露用户信息
                String token = JwtUtil.generateToken(subject, 1000 * 60 * 5);
                // 写入redis
                redisTemplate
                        .opsForValue()
                        .set(
                                subject,
                                authentication.getPrincipal(),
                                1000 * 60 * 5,
                                TimeUnit.MILLISECONDS);
                return Result.success(token); //登陆成功直接返回token信息
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.failure("unauthorized");
    }
}
```

###### 1.7 自定义LoginController

```java
@RestController
public class LoginController {
    @Autowired private LoginService loginService;

    @CrossOrigin //前后端分离项目中解决跨域请求的注解
    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginUser loginUser) {
        return loginService.login(loginUser);
    }
}
```



###### 1.8 自定义LogoutService

```java
@Service
public class LogoutService {
    @Autowired RedisTemplate redisTemplate;

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
```

###### 1.9 自定义LogoutController

```java
@RestController
public class LogoutController {
    @Autowired private LogoutService logoutService;

    @CrossOrigin
    @GetMapping("/logout")
    public Result<String> logout() {
        return logoutService.logout();
    }
}
```



###### 1.10 postman测试流程

- "/login"验证用户名密码(body/raw)，获得token

- Header中带token，访问认证后可获取的资源

  

###### 1.11 小结

> - 首先进行login操作，认证成功后将Authentication存入SecurityContextHolder，并生成token令牌，将其存入redis中，用于后续访问资源（Redis通常用于存储会话信息，减轻服务器的内存负担，或者用于分布式系统的会话共享；而SecurityContextHolder用于在当前线程中保持认证信息，以便快速访问。两者一般可以同时使用。）
> - 访问资源的过程中需要在Header中携带刚刚生成的token令牌，然后经过SecurityFilterChain进行安全验证。其中，JwtAuthenticationFilter用于校验令牌，从请求头中获取token，然后解析出用户信息，并从redis中进行读取，若读取失败，则直接返回错误信息；若读取成功，则生成新的Authentication存入安全上下文，并且放行该请求
> - 进行logout操作的逻辑：清除redis中对应用户信息，与登陆逻辑正好相反。登出操作后，令牌失效，无法再访问资源。



##### 2. 前端用户名密码登陆+OAuth2第三方登陆(以github为例)

###### 2.1 User表和自定义的UserDetails表同上（用于表单登录）

###### 2.1 基于数据库的信息管理器同上

###### 2.3 配置第三方认证的client注册信息

```yaml
#在Github官网进行client注册，得到client_id和client_secret，剩下配置保持github默认
spring: 
  security:
      oauth2:
        client:
          registration:
            github:
              client-id: Ov23liHthNDpsR6Efdi4
              client-secret: ffd78c1469c885b8da5247a06c9becf9e09a6767
```



###### 2.4 Oauth2SecurityConfig

```java
@Configuration
@EnableMethodSecurity // 开启基于方法的授权
public class Oauth2SecurityConfig {
    @Autowired private DBUserDetailsManager manager;
    @Autowired private OAuth2UserServiceImpl userService;
    @Autowired private GrantedAuthoritiesMapper userAuthoritiesMapper;

  //引入基于数据库的信息管理器（同上）
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(manager);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }

  //自定义的Handler可以返回封装好的Json信息以代替各种EndPoint，可用于前后端分析系统
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());
      //开启oauth2登陆
        http.oauth2Login(
                oauth ->
                        oauth.userInfoEndpoint(
                                userInfo ->
                          							//为新登陆用户定义权限
                                        userInfo.userAuthoritiesMapper(userAuthoritiesMapper)
                          												//将登陆信息存入第三方登录信息表
                                                .userService(userService))
          										//自定义登陆成功信息
        											.successHandler(new MyAuthenticationSuccessHandler())
          										//自定义登陆失败信息
                            	.failureHandler(new MyAuthenticationFailureHandler()));
        //同时开启表单登陆
      http.formLogin(
                form ->
        								//自定义登陆成功信息
                        form.successHandler(new MyAuthenticationSuccessHandler()) 
       									 //自定义登陆失败信息
                            .failureHandler(new MyAuthenticationFailureHandler())); 
      //自定义注销信息
        http.logout(logout -> logout.logoutSuccessHandler(new MyLogoutSuccessHandler())); 
        http.exceptionHandling(
                exception ->
                        exception
          											//自定义未授权请求返回信息
                                .accessDeniedHandler(new MyAccessDeniedHandler())); 
        http.sessionManagement(
                session ->
                        session
                                // 最多允许1个设备同时在线，后登陆的设备会把先前设备挤掉
                                .maximumSessions(1)
                                // 自定义账号同时登陆多次返回信息
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
```



###### 2.5 为新登陆用户授权（Oauth2第三方登录）

```java
@Service
public class GrantedAuthoritiesMapperImpl implements GrantedAuthoritiesMapper {
    @Override
    public Collection<? extends GrantedAuthority> mapAuthorities(
            Collection<? extends GrantedAuthority> authorities) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return grantedAuthorities;
    }
```



###### 2.6 建立第三方登录信息实体类

```java
@TableName(value = "oauth2_third_account")
@Data
public class Oauth2ThirdAccount implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer uniqueId;
    private String login;
    private String name;
    private String avatarUrl;
    private String credentials;
    private Date credentialsExpiresAt;
    private String registrationId;
    
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
```

###### 2.7 将新登陆用户转化为第三方登录信息

```java
public class GithubUserConverter {
    public static Oauth2ThirdAccount convert(OAuth2UserRequest userRequest, OAuth2User oauth2User) {
        Oauth2ThirdAccount account = new Oauth2ThirdAccount();
        Map<String, Object> attributes = oauth2User.getAttributes();
        account.setUniqueId((Integer) attributes.get("id"));
        account.setLogin((String) attributes.get("login"));
        account.setName((String) attributes.get("name"));
        account.setAvatarUrl((String) attributes.get("avatar_url"));
        account.setRegistrationId("github");
        OAuth2AccessToken accessToken = userRequest.getAccessToken();
        account.setCredentials(accessToken.getTokenValue());
        ZonedDateTime zonedDateTime = accessToken.getExpiresAt().atZone(ZoneId.systemDefault());
        account.setCredentialsExpiresAt(Date.from(zonedDateTime.toInstant()));
        account.setCreateTime(new Date());
        return account;
    }
}
```

###### 2.8 将第三方登录信息存入数据库

```java
@Service
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {
    @Autowired private Oauth2ThirdAccountMapper mapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Oauth2ThirdAccount account = null;
        if ("github".equals(registrationId)) {
            account = GithubUserConverter.convert(userRequest, oauth2User);
            QueryWrapper<Oauth2ThirdAccount> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("unique_id", account.getUniqueId());
            if (mapper.selectOne(queryWrapper) == null) {
                mapper.insert(account);
            }
        }
        return oauth2User;
    }
}
```



###### 2.9 小结

> - 首先在第三方授权应用上进行client注册，并配置client相关信息
> - login操作中，认证成功后为用户进行初始化的授权，然后将用户信息转化为第三方登录信息存入数据库
> - 表单登录和Oauth2第三方登录可以同时开启
