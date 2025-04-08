package com.shuting.flowEdaProgram.commons.exception;

import com.shuting.flowEdaProgram.commons.http.ApiError;

public class MissingPropertyException extends FlowException {
    public MissingPropertyException(String name) {
        super(
                ApiError.MISS_PROPERTY_IN_BODY,
                String.format("Request property '%s' in request body", name));
    }
}
