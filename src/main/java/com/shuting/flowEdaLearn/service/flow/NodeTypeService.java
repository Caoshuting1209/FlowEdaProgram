package com.shuting.flowEdaLearn.service.flow;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.shuting.flowEdaLearn.entity.flow.NodeType;
import com.shuting.flowEdaLearn.mapper.flow.NodeTypeMapper;
import com.shuting.flowEdaLearn.mapper.flow.NodeTypeParamsMapper;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class NodeTypeService extends ServiceImpl<NodeTypeMapper, NodeType> {
    private static final List<String> MENU = Arrays.asList("基础", "运算", "解析", "网络", "数据库", "子流程");
    @Autowired private NodeTypeMapper nodeTypeMapper;
    @Autowired private NodeTypeParamsMapper nodeTypeParamsMapper;

    public Map<String, Object> getAllNodeTypes() {
        Map<String, Object> result = new HashMap<String, Object>();
        List<NodeType> list = nodeTypeMapper.selectList(null);
        //        list.forEach(this::mergeNodeType);
        MENU.forEach(
                k -> result.put(k, list.stream().filter(nodeType -> k.equals(nodeType.getMenu()))));
        return result;
    }

    // 以下代码只在初始化NodeType的params时运行一次，之后的数据读取不需要用到
    //        private void mergeNodeType(NodeType nodeType) {
    //            List<NodeTypeParams> list = nodeTypeParamsMapper.findByTypeId(nodeType.getId());
    //            nodeType.setParams(list);
    //            this.saveOrUpdate(nodeType);
    //        }

    //        private List<NodeTypeParams> findByTypeId(Long typeId) {
    //            QueryWrapper<NodeTypeParams> queryWrapper = new QueryWrapper<>();
    //            queryWrapper.eq("type_id", typeId);
    //            return nodeTypeParamsMapper.selectList(queryWrapper);
    //        }
}
