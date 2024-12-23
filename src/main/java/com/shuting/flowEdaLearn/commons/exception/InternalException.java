package com.shuting.flowEdaLearn.commons.exception;

import com.shuting.flowEdaLearn.commons.http.ApiError;

public class InternalException extends FlowException {
    public InternalException(String message) {
        super(ApiError.INTERNAL_ERROR, message);
    }
}
