package com.shuting.flowEdaProgram;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.shuting.flowEdaProgram.flow.mapper", "com.shuting.flowEdaProgram.security.user.mapper"})
public class FlowEdaLearnApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowEdaLearnApplication.class, args);
    }
}
