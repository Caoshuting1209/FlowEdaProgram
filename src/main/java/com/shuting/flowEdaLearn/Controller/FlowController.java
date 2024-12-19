package com.shuting.flowEdaLearn.Controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shuting.flowEdaLearn.commons.http.Result;
import com.shuting.flowEdaLearn.entity.Flow;
import com.shuting.flowEdaLearn.entity.FlowRequest;
import com.shuting.flowEdaLearn.service.FlowService;
import com.shuting.flowEdaLearn.validation.DeleteGroup;
import com.shuting.flowEdaLearn.validation.PostGroup;
import com.shuting.flowEdaLearn.validation.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class FlowController {
    @Autowired private FlowService flowService;

    @GetMapping("/flow")
    public Result<IPage<Flow>> listFlow(@RequestBody FlowRequest flowRequest) {
        return flowService.listFlow(flowRequest);
    }

    @PostMapping("/flow")
    public Result<Flow> postFlow(@Validated({PostGroup.class})  @RequestBody Flow flow) {
        return flowService.postFlow(flow);
    }

    @PutMapping("/flow")
    public Result<Flow> updateFlow(@Validated({UpdateGroup.class})  @RequestBody Flow flow) {
        return flowService.updateFlow(flow);
    }

    @DeleteMapping("/flow")
    public Result<Flow> deleteFlow(@Validated({DeleteGroup.class})  @RequestBody Flow flow) {
        return flowService.deleteFlow(flow);
    }
}
