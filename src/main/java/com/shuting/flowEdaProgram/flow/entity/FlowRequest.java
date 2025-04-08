package com.shuting.flowEdaProgram.flow.entity;

import com.shuting.flowEdaProgram.commons.http.PageRequest;

import lombok.Data;

@Data
public class FlowRequest extends PageRequest {
    private String name;
    private Flow.Status status;
}
