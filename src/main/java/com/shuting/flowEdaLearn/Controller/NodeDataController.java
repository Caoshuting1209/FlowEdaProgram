package com.shuting.flowEdaLearn.Controller;

import com.shuting.flowEdaLearn.commons.http.Result;
import com.shuting.flowEdaLearn.entity.NodeData;
import com.shuting.flowEdaLearn.service.NodeDataService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class NodeDataController {
    @Autowired private NodeDataService nodeDataService;

    @PostMapping("/node/data")
    public void saveNodeData(@RequestBody List<NodeData> list) {
        nodeDataService.saveNodeData(list);
    }

    @GetMapping("/node/data")
    public List<NodeData> getNodeData(String flowId) {
        return nodeDataService.getNodeData(flowId);
    }
}
