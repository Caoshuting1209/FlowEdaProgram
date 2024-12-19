package com.shuting.flowEdaLearn.commons.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private T result;

    public static <T> Result<T> done(T result) {
        return new Result<T>(result);
    }
}
