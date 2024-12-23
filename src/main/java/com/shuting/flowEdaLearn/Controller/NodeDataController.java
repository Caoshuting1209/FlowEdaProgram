package com.shuting.flowEdaLearn.Controller;

import com.shuting.flowEdaLearn.entity.NodeData;
import com.shuting.flowEdaLearn.service.NodeDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class NodeDataController {
    @Autowired private NodeDataService nodeDataService;

    @PostMapping("/node/data")
    public void setNodeData(List<NodeData> list) {
        nodeDataService.updateNodeData(list);
    }

    @GetMapping("/node/data")
    public List<NodeData> getNodeData(String flowId) {
        return nodeDataService.getNodeData(flowId);
    }

    @PostMapping("/node/data/version")
    public void setVersion(String version, List<NodeData> nodeDataList) {
        nodeDataService.setVersion(version, nodeDataList);
    }

    @GetMapping("/node/data/version")
    public List<NodeData> getVersion(String flowId, String version) {
        return nodeDataService.getVersion(flowId, version);
    }
}
