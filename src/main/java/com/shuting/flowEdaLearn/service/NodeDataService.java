package com.shuting.flowEdaLearn.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shuting.flowEdaLearn.commons.exception.InvalidParameterException;
import com.shuting.flowEdaLearn.commons.exception.MissingPropertyException;
import com.shuting.flowEdaLearn.entity.NodeData;
import com.shuting.flowEdaLearn.mapper.NodeDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@Slf4j
public class NodeDataService extends ServiceImpl<NodeDataMapper, NodeData> {
    @Autowired private NodeDataMapper nodeDataMapper;
    //这边是已经有了List<NodeData> list数据，进行保存
    //那么这个List<NodeData> 中的NodeData是怎么来的？
    //已知其中的大部分字段可以自动生成，但params需要客户端赋值
    //那么可能有这样的函数：NodeData generate(List<String> list),list中依次是params中的key对应的value值
    //然后新建一个NodeData对象，把它的Map<String, String>类的params字段设置为NodeData的params
    //然后把这个NodeData对象存入List<NodeData> list
    //当点击保存时，才是save的操作，这时候我们明确params字段是Map<list, list>类型，可以写一个typeHandler，使之与json进行交互
    //这涉及到嵌套函数调用，这里不展开
    public void savaNodeData(List<NodeData> list) {
        check(list);
        list.forEach(nodeDataMapper::insert);
    }

    public void updateNodeData(List<NodeData> list) {
        check(list);
        String flowId = list.get(0).getFlowId();
        nodeDataMapper.deleteByFlowId(flowId);
        list.forEach(nodeDataMapper::insert);
    }

    public List<NodeData> getNodeData(String flowId) {
        List<NodeData> list = nodeDataMapper.findByFlowId(flowId);
        return list;
    }

    public void setVersion(String version, List<NodeData> list) {

        if (version.length() > 32) {
            throw new InvalidParameterException("Version name is too long");
        }
        check(list);
        list.forEach(
                node -> {
                    node.setVersion(version);
                });
        savaNodeData(list);
    }

    public List<NodeData> getVersion(String flowId, String version) {
        List<NodeData> list;
        if (version != null) {
            list = nodeDataMapper.findByFlowIdAndVersion(flowId, version);
        } else {
            list = nodeDataMapper.findByFlowId(flowId);
        }
        return list;
    }

    private void check(List<NodeData> list) {
        if (CollectionUtils.isEmpty(list)) {
            throw new InvalidParameterException("list is empty");
        }
        for (NodeData nodeData : list) {
            if (nodeData.getId() == null) {
                log.error("id is null");
                throw new MissingPropertyException("id");
            }
            if (nodeData.getFlowId() == null) {
                log.error("flow_id is null");
                throw new MissingPropertyException("flow_id");
            }
        }
    }


    //    private List<NodeData> findByFlowId(String flowId) {
    //        QueryWrapper<NodeData> queryWrapper = new QueryWrapper<>();
    //        queryWrapper.eq("flow_id", flowId);
    //        return nodeDataMapper.selectList(queryWrapper);
    //    }
    //
    //    private void deleteByFlowId(String flowId) {
    //        QueryWrapper<NodeData> queryWrapper = new QueryWrapper<>();
    //        queryWrapper.eq("flow_id", flowId);
    //        nodeDataMapper.delete(queryWrapper);
    //    }
    //
    //    private List<NodeData> findByFlowIdAndVersion(String flowId, String version) {
    //        QueryWrapper<NodeData> queryWrapper = new QueryWrapper<>();
    //        queryWrapper.eq("flow_id", flowId);
    //        queryWrapper.eq("version", version);
    //        return nodeDataMapper.selectList(queryWrapper);
    //    }
}
