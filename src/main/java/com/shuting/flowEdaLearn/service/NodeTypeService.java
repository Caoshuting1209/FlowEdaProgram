package com.shuting.flowEdaLearn.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shuting.flowEdaLearn.entity.NodeType;
import com.shuting.flowEdaLearn.entity.NodeTypeParams;
import com.shuting.flowEdaLearn.mapper.NodeTypeMapper;
import com.shuting.flowEdaLearn.mapper.NodeTypeParamsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class NodeTypeService {
    private static final List<String> MENU = Arrays.asList("基础", "运算", "解析", "网络", "数据库", "子流程");
    @Autowired private NodeTypeMapper nodeTypeMapper;
    @Autowired private NodeTypeParamsMapper nodeTypeParamsMapper;

    public Map<String, Object> getAllNodeTypes() {
        Map<String, Object> result = new HashMap<String, Object>();
        List<NodeType> list = nodeTypeMapper.selectList(null);
        list.forEach(this::mergeNodeType);
        MENU.forEach(
                k ->
                        result.put(
                                k, list.stream().filter(nodeType -> k.equals(nodeType.getMenu()))));
        return result;
    }

    private void mergeNodeType(NodeType nodeType) {
        List<NodeTypeParams> list = nodeTypeParamsMapper.findByTypeId(nodeType.getId());
        nodeType.setParams(list);
    }

     //当前这种方法无法在postman正常实现查询功能，但在mapper层用sql注解可以正常查询，可能是数据库格式和java对象格式转换的问题
//    private List<NodeTypeParams> findByTypeId(Long typeId) {
//        QueryWrapper<NodeTypeParams> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("type_id", typeId);
//        return nodeTypeParamsMapper.selectList(queryWrapper);
//    }
}
