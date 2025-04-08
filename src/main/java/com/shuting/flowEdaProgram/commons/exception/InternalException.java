package com.shuting.flowEdaProgram.commons.exception;

import com.shuting.flowEdaProgram.commons.http.ApiError;

public class InternalException extends FlowException {
    public InternalException(String message) {
        super(ApiError.INTERNAL_ERROR, message);
    }
}
