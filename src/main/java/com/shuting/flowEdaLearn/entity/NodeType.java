package com.shuting.flowEdaLearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@TableName(value = "eda_flow_node_type")
public class NodeType {
    private Long id;
    private String type;
    private String typeName;
    private String menu;
    private String description;
    private String background;
    private String svg;
    private List<NodeTypeParams> params;
}
