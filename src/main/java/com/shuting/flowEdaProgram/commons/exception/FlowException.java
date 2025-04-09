package com.shuting.flowEdaProgram.commons.exception;

import lombok.Data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
public class FlowException extends RuntimeException {
    private String error;
    private String message;
    private HttpStatus httpStatus;

    public FlowException(String error, String message) {
        super(message);
        this.error = error;
        this.message = message;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }
}