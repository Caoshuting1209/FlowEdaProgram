package com.shuting.flowEdaLearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;


import lombok.Data;

import java.util.List;
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
    private List<Map<String, String>> params;
    private List<Map<String, String>> payload;
    private String from;
    private String to;
    private String version;
}
