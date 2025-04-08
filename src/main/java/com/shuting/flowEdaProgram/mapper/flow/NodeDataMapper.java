package com.shuting.flowEdaProgram.mapper.flow;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shuting.flowEdaProgram.entity.flow.NodeData;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NodeDataMapper extends BaseMapper<NodeData> {
    @Select("SELECT * FROM eda_flow_node_data WHERE flow_id = #{flowId}")
    List<NodeData> findByFlowId(String flowId);

    @Delete("DELETE FROM eda_flow_node_data WHERE flow_id = #{flowId}")
    void deleteByFlowId(String flowId);

    @Select("SELECT * FROM eda_flow_node_data WHERE flow_id = #{flowId} AND version = #{version}")
    List<NodeData> findByFlowIdAndVersion(String flowId, String version);

    @Select("SELECT params FROM eda_flow_node_data WHERE id = #{id}")
    String findParamsById(String id);

    @Select("SELECT payload FROM eda_flow_node_data WHERE id = #{id}")
    String findPayloadById(String id);
}
