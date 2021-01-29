package com.goldcrestwilma;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class ExtractTransformLoadApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExtractTransformLoadApplication.class, args);
    }
}
