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
  

###### 6.3 数据库json格式的数据与javaObject的交互

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
  
  

###### 6.4 用java方法代替手写SQL

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



