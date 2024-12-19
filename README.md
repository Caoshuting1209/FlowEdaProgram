###  Flow_eda_learning

##### 1. 新建项目，注入依赖，配置

##### 2. 定义实体类和接口

##### 3. 交互数据库，在service层实现增删改查功能

##### 4. 全局异常处理

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
//分模块异常类拓展
public class InvalidParameterException extends FlowException {
    public InvalidParameterException(String message) {
        super(ApiError.INVALID_PARAMETER, message);
    }
}

//异常处理器
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
      //body的内容即为前端报错信息
        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    private String getURI(WebRequest request) {
        if (request instanceof ServletWebRequest) {
            return ((ServletWebRequest) request).getRequest().getRequestURI();
        }
        return null;
    }
}
```

##### 5. 完善流程节点业务

###### 5.1 节点类型（NodeType）接口定义

###### 5.2 节点数据（NodeData）接口定义



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



###### 6.2 参数分组校验

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
        //这里可以返回校验失败的具体参数信息，但必须自定义error捕捉方法，否则只会返回给客户端默认的校验失败信息，而不会包含具体参数
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

  



