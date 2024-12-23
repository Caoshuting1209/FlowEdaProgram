package com.shuting.flowEdaLearn.commons.exception;

import com.shuting.flowEdaLearn.commons.http.ApiError;

public class InvalidParameterException extends FlowException {
    public InvalidParameterException(String message) {
        super(ApiError.INVALID_PARAMETER, message);
    }

    public InvalidParameterException(String name, Object value) {
        super(ApiError.INVALID_PARAMETER, String.format("Invalid parameter '%s': %s", name, value));
    }
}
