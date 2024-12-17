package com.shuting.flowEdaLearn.Controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shuting.flowEdaLearn.entity.Flow;
import com.shuting.flowEdaLearn.entity.FlowRequest;
import com.shuting.flowEdaLearn.service.FlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class FlowController {
    @Autowired
    private FlowService flowService;

    @GetMapping("/flow")
    @ResponseBody
    public IPage<Flow> listFlow(FlowRequest flowRequest) {
        return flowService.listFlow(flowRequest);
    }

    @PostMapping("/flow")
    @ResponseBody
    public Flow postFlow(Flow flow) {
        return flowService.postFlow(flow);
    }

    @PutMapping("/flow")
    @ResponseBody
    public Flow updateFlow(Flow flow) {
        return flowService.updateFlow(flow);
    }

    @DeleteMapping("/flow")
    @ResponseBody
    public Flow deleteFlow(Flow flow) {
       return flowService.deleteFlow(flow);
    }
}
