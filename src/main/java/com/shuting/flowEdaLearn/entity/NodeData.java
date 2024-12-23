package com.shuting.flowEdaLearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import javax.swing.text.Document;
import java.util.Map;

@Data
@TableName(value = "eda_flow_node_data")
public class NodeData {
    private String id;
    private String nodeName;
    private String flowId;
    private Long typeId;
    private String top;
    private String left;
    private String remark;
    private Map<String, Object> params;
    private Map<String, Object> payload;
    private String from;
    private String to;
    private String version;
}
