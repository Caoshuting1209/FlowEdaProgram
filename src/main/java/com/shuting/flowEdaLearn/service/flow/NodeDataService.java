package com.shuting.flowEdaLearn.service.flow;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shuting.flowEdaLearn.commons.exception.InvalidParameterException;
import com.shuting.flowEdaLearn.commons.exception.MissingPropertyException;
import com.shuting.flowEdaLearn.entity.flow.NodeData;

import com.shuting.flowEdaLearn.mapper.flow.NodeDataMapper;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@Slf4j
public class NodeDataService extends ServiceImpl<NodeDataMapper, NodeData> {
    @Autowired private NodeDataMapper nodeDataMapper;

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
