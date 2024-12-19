package com.shuting.flowEdaLearn.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shuting.flowEdaLearn.commons.http.Result;
import com.shuting.flowEdaLearn.entity.Flow;
import com.shuting.flowEdaLearn.entity.FlowRequest;
import com.shuting.flowEdaLearn.mapper.FlowMapper;
import com.shuting.flowEdaLearn.validation.DeleteGroup;
import com.shuting.flowEdaLearn.validation.PostGroup;
import com.shuting.flowEdaLearn.validation.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class FlowService {
    @Autowired private FlowMapper flowMapper;

    public Result<IPage<Flow>> listFlow(FlowRequest flowRequest) {
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
        return new Result<IPage<Flow>>(resPage);
    }

    public Result<Flow> postFlow(Flow flow) {
        flow.setStatus(Flow.Status.INIT);
        flowMapper.insert(flow);
        return new Result<Flow>(flow);
    }

    public Result<Flow> updateFlow(Flow flow) {
        flowMapper.updateById(flow);
        return new Result<Flow>(flow);
    }

    public Result<Flow> deleteFlow(Flow flow) {
        flowMapper.deleteById(flow.getId());
        return new Result<Flow>(flow);
    }
}
