package com.shuting.flowEdaLearn.service;

import com.shuting.flowEdaLearn.commons.exception.MissingPropertyException;
import com.shuting.flowEdaLearn.entity.NodeData;
import com.shuting.flowEdaLearn.mapper.NodeDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NodeDataService {
    @Autowired private NodeDataMapper nodeDataMapper;

    public void saveNodeData(List<NodeData> list) {
        list.forEach(this::check);
        list.forEach(nodeDataMapper::insert);
    }
    public List<NodeData> getNodeData(String flowId) {
        return nodeDataMapper.findByFlowId(flowId);
    }

    private void check(NodeData nodeData) {
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
