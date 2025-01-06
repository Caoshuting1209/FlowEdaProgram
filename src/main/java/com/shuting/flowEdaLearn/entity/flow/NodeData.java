package com.shuting.flowEdaLearn.entity.flow;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
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

    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, String> params;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, String> payload;

    private String from;
    private String to;
    private String version;
}
