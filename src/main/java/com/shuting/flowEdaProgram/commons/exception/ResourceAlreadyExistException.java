package com.shuting.flowEdaProgram.commons.exception;

import com.shuting.flowEdaProgram.commons.http.ApiError;

public class ResourceAlreadyExistException extends FlowException {
    public ResourceAlreadyExistException(String message) {
        super(ApiError.RESOURCE_ALREADY_EXIST, message);
    }
}
