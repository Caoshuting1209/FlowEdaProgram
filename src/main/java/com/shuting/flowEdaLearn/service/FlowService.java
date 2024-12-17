package com.shuting.flowEdaLearn.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shuting.flowEdaLearn.entity.Flow;
import com.shuting.flowEdaLearn.entity.FlowRequest;
import com.shuting.flowEdaLearn.mapper.FlowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
public class FlowService {
    @Autowired
    private FlowMapper flowMapper;

    public IPage<Flow> listFlow(FlowRequest flowRequest) {
        Page<Flow> page = new Page<>(flowRequest.getPage(), flowRequest.getLimit());
        QueryWrapper<Flow> queryWrapper = new QueryWrapper<>();
        if (flowRequest.getName() != null) {
            queryWrapper.eq("name", flowRequest.getName());
        }
        if (flowRequest.getStatus() != null) {
            queryWrapper.eq("status", flowRequest.getStatus());
        }
        queryWrapper.orderByAsc("create_time");
        IPage<Flow> resPage = flowMapper.selectPage(page, queryWrapper);
        return resPage;
    }

    public Flow postFlow(@Validated Flow flow) {
        flow.setStatus(Flow.Status.INIT);
        flowMapper.insert(flow);
        return flow;
    }

    public Flow updateFlow(@Validated Flow flow) {
        flowMapper.updateById(flow);
        return flow;
    }
    public Flow deleteFlow(@Validated Flow flow) {
        flowMapper.deleteById(flow.getId());
        return flow;
    }
}
