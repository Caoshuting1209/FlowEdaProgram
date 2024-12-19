package com.shuting.flowEdaLearn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.shuting.flowEdaLearn.validation.PostGroup;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import javax.swing.text.Document;

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
    private Document params;
    private Document payload;
    private String from;
    private String to;
    private String version;
}
