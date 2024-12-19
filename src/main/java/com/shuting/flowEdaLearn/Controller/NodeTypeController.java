package com.shuting.flowEdaLearn.Controller;

import com.shuting.flowEdaLearn.commons.http.Result;
import com.shuting.flowEdaLearn.entity.NodeType;
import com.shuting.flowEdaLearn.service.NodeTypeService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class NodeTypeController {
    @Autowired private NodeTypeService nodeTypeService;

    @GetMapping("/node/type")
    public Document getNodeType() {
        return nodeTypeService.getAllNodeTypes();
    }
}
