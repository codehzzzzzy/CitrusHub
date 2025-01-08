package com.hzzzzzy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author hzzzzzy
 * @date 2025/1/5
 * @description CitrusHubApplication
 */
@SpringBootApplication
@EnableScheduling
public class CitrusHubApplication {
    public static void main(String[] args) {
        SpringApplication.run(CitrusHubApplication.class, args);
    }
}
