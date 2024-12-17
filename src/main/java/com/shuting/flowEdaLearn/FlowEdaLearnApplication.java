package com.shuting.flowEdaLearn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.shuting.flowEdaLearn.mapper")
public class FlowEdaLearnApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowEdaLearnApplication.class, args);
    }

}
