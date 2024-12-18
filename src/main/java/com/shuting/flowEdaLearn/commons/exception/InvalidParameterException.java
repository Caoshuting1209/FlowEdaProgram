package com.shuting.flowEdaLearn.commons.exception;

import com.shuting.flowEdaLearn.commons.http.ApiError;

public class InvalidParameterException extends FlowException {
    public InvalidParameterException(String message) {
        super(ApiError.INVALID_PARAMETER, message);
    }
}
