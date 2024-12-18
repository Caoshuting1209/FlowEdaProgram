package com.shuting.flowEdaLearn.Controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shuting.flowEdaLearn.commons.http.Result;
import com.shuting.flowEdaLearn.entity.Flow;
import com.shuting.flowEdaLearn.entity.FlowRequest;
import com.shuting.flowEdaLearn.service.FlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class FlowController {
    @Autowired
    private FlowService flowService;

    @GetMapping("/flow")
    @ResponseBody
    public Result<IPage<Flow>> listFlow(FlowRequest flowRequest) {
        return flowService.listFlow(flowRequest);
    }

    @PostMapping("/flow")
    @ResponseBody
    public Result<Flow> postFlow(Flow flow) {
        return flowService.postFlow(flow);
    }

    @PutMapping("/flow")
    @ResponseBody
    public Result<Flow> updateFlow(Flow flow) {
        return flowService.updateFlow(flow);
    }

    @DeleteMapping("/flow")
    @ResponseBody
    public Result<Flow> deleteFlow(Flow flow) {
       return flowService.deleteFlow(flow);
    }
}
