package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class Javawebkcsj3Application {

    public static void main(String[] args) {
        SpringApplication.run(Javawebkcsj3Application.class, args);
    }

}
