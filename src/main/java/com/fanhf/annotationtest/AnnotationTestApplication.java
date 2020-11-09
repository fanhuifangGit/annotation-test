package com.fanhf.annotationtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AnnotationTestApplication {
    private static final Logger log = LoggerFactory.getLogger(AnnotationTestApplication.class);

    public AnnotationTestApplication() {}

    public static void main(String[] args) {
        SpringApplication.run(AnnotationTestApplication.class, args);
        log.info("启动成功!!!!!!");
    }
}
