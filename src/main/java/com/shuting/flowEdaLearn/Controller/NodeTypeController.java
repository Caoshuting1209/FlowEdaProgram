package com.shuting.flowEdaLearn.Controller;

import com.shuting.flowEdaLearn.service.NodeTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class NodeTypeController {
    @Autowired private NodeTypeService nodeTypeService;

    @GetMapping("/node/type")
    public Map<String, Object> getNodeType() {
        return nodeTypeService.getAllNodeTypes();
    }
}
