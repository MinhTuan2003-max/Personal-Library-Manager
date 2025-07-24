package com.luv2code.springboot.demo.plm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.luv2code.springboot.demo.plm.repository")
@EnableMongoAuditing
public class MongoConfig {
    // MongoDB configuration will be handled by Spring Boot auto-configuration
}
