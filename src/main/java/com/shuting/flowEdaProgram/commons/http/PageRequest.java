package com.shuting.flowEdaProgram.commons.http;

import lombok.Data;

@Data
public class PageRequest {
    private int page = 1;
    private int limit = 5;
}
