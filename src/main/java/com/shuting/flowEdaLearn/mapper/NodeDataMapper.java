package com.shuting.flowEdaLearn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shuting.flowEdaLearn.commons.http.Result;
import com.shuting.flowEdaLearn.entity.NodeData;
import org.apache.ibatis.annotations.Select;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NodeDataMapper extends BaseMapper<NodeData> {
    @Select("SELECT * FROM eda_flow_node_data WHERE flow_id = #{flowId}")
    public List<NodeData> findByFlowId(String flowId);
}
