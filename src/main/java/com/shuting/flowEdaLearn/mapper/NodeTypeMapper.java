package com.shuting.flowEdaLearn.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shuting.flowEdaLearn.entity.NodeType;
import com.shuting.flowEdaLearn.entity.NodeTypeParams;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NodeTypeMapper extends BaseMapper<NodeType> {
    @Select("SELECT params FROM eda_flow_node_type WHERE id = #{id}")
    String findById(Long id);

    @Select("SELECT * FROM eda_flow_node_type WHERE id = #{id}")
    List<NodeType> selectById(Long id);

    @Update("UPDATE eda_flow_node_type SET params = #{paramsJson} WHERE id = #{id}")
    void insertParams(Long id, String paramsJson);
}
