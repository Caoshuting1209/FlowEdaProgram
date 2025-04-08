package com.shuting.flowEdaProgram.flow.controller;


import com.shuting.flowEdaProgram.flow.entity.NodeData;
import com.shuting.flowEdaProgram.flow.service.NodeDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class NodeDataController {
    @Autowired private NodeDataService nodeDataService;

    @CrossOrigin
    @PostMapping("/node/data")
    public void setNodeData(List<NodeData> list) {
        nodeDataService.updateNodeData(list);
    }

    @CrossOrigin
    @GetMapping("/node/data")
    public List<NodeData> getNodeData(String flowId) {
        return nodeDataService.getNodeData(flowId);
    }

    @CrossOrigin
    @PostMapping("/node/data/version")
    public void setVersion(String version, List<NodeData> nodeDataList) {
        nodeDataService.setVersion(version, nodeDataList);
    }

    @CrossOrigin
    @GetMapping("/node/data/version")
    public List<NodeData> getVersion(String flowId, String version) {
        return nodeDataService.getVersion(flowId, version);
    }
}
