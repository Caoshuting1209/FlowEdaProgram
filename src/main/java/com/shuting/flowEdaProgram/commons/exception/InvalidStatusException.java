package com.shuting.flowEdaProgram.commons.exception;

import com.shuting.flowEdaProgram.commons.http.ApiError;

public class InvalidStatusException extends FlowException {
    public InvalidStatusException(String message) {
        super(ApiError.INVALID_STATUS, message);
    }
}
