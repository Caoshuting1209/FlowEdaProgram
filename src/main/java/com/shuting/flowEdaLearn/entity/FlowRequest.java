package com.shuting.flowEdaLearn.entity;

import com.shuting.flowEdaLearn.commons.PageRequest;
import lombok.Data;

@Data
public class FlowRequest extends PageRequest {
    private String name;
    private Flow.Status status;
}
