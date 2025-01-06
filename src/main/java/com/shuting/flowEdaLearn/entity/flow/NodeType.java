package com.shuting.flowEdaLearn.entity.flow;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.shuting.flowEdaLearn.commons.config.typeHandler.NodeTypeParamsTypeHandler;

import lombok.Data;

import java.util.List;

@Data
@TableName(value = "eda_flow_node_type", autoResultMap = true)
public class NodeType {
    private Long id;
    private String type;
    private String typeName;
    private String menu;
    private String description;
    private String background;
    private String svg;

    @TableField(typeHandler = NodeTypeParamsTypeHandler.class)
    private List<NodeTypeParams> params;
}
