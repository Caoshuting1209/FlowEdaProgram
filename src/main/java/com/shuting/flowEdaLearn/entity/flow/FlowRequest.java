package com.shuting.flowEdaLearn.entity.flow;

import com.shuting.flowEdaLearn.commons.http.PageRequest;
import lombok.Data;

@Data
public class FlowRequest extends PageRequest {
    private String name;
    private Flow.Status status;
}
