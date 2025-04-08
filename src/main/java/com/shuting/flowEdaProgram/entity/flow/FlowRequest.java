package com.shuting.flowEdaProgram.entity.flow;

import com.shuting.flowEdaProgram.commons.http.PageRequest;

import lombok.Data;

@Data
public class FlowRequest extends PageRequest {
    private String name;
    private Flow.Status status;
}
