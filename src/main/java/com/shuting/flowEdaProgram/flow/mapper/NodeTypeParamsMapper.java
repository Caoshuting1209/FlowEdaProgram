package com.shuting.flowEdaProgram.flow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.shuting.flowEdaProgram.flow.entity.NodeTypeParams;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NodeTypeParamsMapper extends BaseMapper<NodeTypeParams> {
    @Select("SELECT * FROM eda_flow_node_type_param WHERE type_id = #{typeId}")
    List<NodeTypeParams> findByTypeId(Long typeId);
}
