package com.shuting.flowEdaLearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "eda_flow_node_type_param")
public class NodeTypeParams {
    private Long id;
    private Long typeId;
    private String key;
    private String name;
    private Integer required;
    private String inType;
    private String option;
    private String placeholder;
}
