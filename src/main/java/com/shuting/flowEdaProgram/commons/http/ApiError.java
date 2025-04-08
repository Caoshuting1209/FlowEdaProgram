package com.shuting.flowEdaProgram.commons.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    public static final String INVALID_PARAMETER = "Invalid parameter";
    public static final String INTERNAL_ERROR = "Internal error";
    public static final String RESOURCE_NOT_FOUND = "Resource not found";
    public static final String MISS_PROPERTY_IN_BODY = "Missing property in request body";
    public static final String INVALID_STATUS = "Invalid status";
    public static final String RESOURCE_ALREADY_EXIST = "Resource already exist";

    private String error;
    private String message;
    private Integer code;
    private String path;
}
