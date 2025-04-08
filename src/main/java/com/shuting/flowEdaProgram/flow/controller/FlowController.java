package com.shuting.flowEdaProgram.flow.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shuting.flowEdaProgram.commons.http.Result;

import com.shuting.flowEdaProgram.flow.entity.Flow;
import com.shuting.flowEdaProgram.flow.entity.FlowRequest;
import com.shuting.flowEdaProgram.flow.service.FlowService;
import com.shuting.flowEdaProgram.commons.validation.DeleteGroup;
import com.shuting.flowEdaProgram.commons.validation.PostGroup;
import com.shuting.flowEdaProgram.commons.validation.UpdateGroup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class FlowController {
    @Autowired private FlowService flowService;

    @CrossOrigin
    @GetMapping("/flow")
    public Result<IPage<Flow>> listFlow(FlowRequest flowRequest) {
        return flowService.listFlow(flowRequest);
    }

    @CrossOrigin
    @PostMapping("/flow")
    public Result<Flow> postFlow(@Validated({PostGroup.class}) Flow flow) {
        return flowService.postFlow(flow);
    }

    @CrossOrigin
    @PutMapping("/flow")
    public Result<Flow> updateFlow(@Validated({UpdateGroup.class}) Flow flow) {
        return flowService.updateFlow(flow);
    }

    @CrossOrigin
    @DeleteMapping("/flow")
    public Result<Flow> deleteFlow(@Validated({DeleteGroup.class}) Flow flow) {
        return flowService.deleteFlow(flow);
    }
}
