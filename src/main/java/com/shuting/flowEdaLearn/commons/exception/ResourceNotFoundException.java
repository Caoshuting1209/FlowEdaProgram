package com.shuting.flowEdaLearn.commons.exception;

import com.shuting.flowEdaLearn.commons.http.ApiError;
import org.apache.catalina.core.AprStatus;

public class ResourceNotFoundException extends FlowException {
    public ResourceNotFoundException(String name, Object value) {
        super(ApiError.RESOURCE_NOT_FOUND, String.format("Resource %s %s not found", name, value));
    }
}
