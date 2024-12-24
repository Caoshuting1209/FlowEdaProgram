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
public interface NodeTypeMapper extends BaseMapper<NodeType> {}
