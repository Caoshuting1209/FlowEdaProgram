package com.shuting.flowEdaLearn.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import com.shuting.flowEdaLearn.handler.typeHandler.MapTypeHandler;
import lombok.Data;

import java.util.Map;

@Data
@TableName(value = "eda_flow_node_data", autoResultMap = true)
public class NodeData {
    private String id;
    private String nodeName;
    private String flowId;
    private Long typeId;
    private String top;
    private String left;
    private String remark;
    //以下两个字段需要自定义typeHandler进行数据交互，当前定义的handler无法成功实现json数据自动解析
    //这里应该有个逻辑: 按照NodeData的typeId查询对应的params列表，得到一个List<NodeTypeParams>
    //初始化新建一个Map，针对以上每个NodeTypeParams，提取其中的key作为map的key值，初始value赋值为null，这个map就是这里params的初始值
    //假设用户新增两个NodeData,typeId是默认生成的，那么可以根据NodeDataService中注解的逻辑为NodeData的params进行赋值
    //数据是先存入，再读取，所有存入的是Map类型，读取解析为同样的类型，一定可以成功
    @TableField(typeHandler = MapTypeHandler.class)
    private Map<String, String> params;
    @TableField(typeHandler = MapTypeHandler.class)
    private Map<String, String> payload;
    private String from;
    private String to;
    private String version;
}
