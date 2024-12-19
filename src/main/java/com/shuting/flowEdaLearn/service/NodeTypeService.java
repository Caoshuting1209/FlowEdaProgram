package com.shuting.flowEdaLearn.service;

import com.shuting.flowEdaLearn.entity.NodeType;
import com.shuting.flowEdaLearn.entity.NodeTypeParams;
import com.shuting.flowEdaLearn.mapper.NodeTypeMapper;
import com.shuting.flowEdaLearn.mapper.NodeTypeParamsMapper;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class NodeTypeService {
    private static final List<String> MENU = Arrays.asList("基础", "运算", "解析", "网络", "数据库", "子流程");
    @Autowired private NodeTypeMapper nodeTypeMapper;
    @Autowired private NodeTypeParamsMapper nodeTypeParamsMapper;

    public Document getAllNodeTypes() {
        Document result = new Document();
        List<NodeType> list = nodeTypeMapper.selectList(null);
        list.forEach(this::mergeNodeType);
        MENU.forEach(
                k ->
                        result.append(
                                k, list.stream().filter(nodeType -> k.equals(nodeType.getMenu()))));
        return result;
    }

    public void mergeNodeType(NodeType nodeType) {
        List<NodeTypeParams> list = nodeTypeParamsMapper.findByTypeId(nodeType.getId());
        nodeType.setParams(list);
    }
}
