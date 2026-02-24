package com.property.management;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.property.management.mapper")
public class PropertyManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(PropertyManagementApplication.class, args);
    }
}
