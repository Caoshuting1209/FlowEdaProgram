package com.shuting.flowEdaLearn.commons.exception;

import com.shuting.flowEdaLearn.commons.http.ApiError;

public class InvalidStatusException extends FlowException {
    public InvalidStatusException(String message) {
        super(ApiError.INVALID_STATUS, message);
    }
}
