package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MicroBlogServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroBlogServerApplication.class, args);
    }

}
