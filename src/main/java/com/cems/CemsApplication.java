package com.cems;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CemsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CemsApplication.class, args);
    }
}
