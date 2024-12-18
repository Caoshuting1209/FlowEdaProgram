###  Flow_eda_learning

#### 分页方法的实现（springboot 3）

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



#### 全局异常处理

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



