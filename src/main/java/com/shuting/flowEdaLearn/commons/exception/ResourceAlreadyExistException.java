package com.shuting.flowEdaLearn.commons.exception;

import com.shuting.flowEdaLearn.commons.http.ApiError;

public class ResourceAlreadyExistException extends FlowException {
    public ResourceAlreadyExistException(String message) {
        super(ApiError.RESOURCE_ALREADY_EXIST, message);
    }
}
