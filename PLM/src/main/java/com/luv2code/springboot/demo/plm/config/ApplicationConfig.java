package com.luv2code.springboot.demo.plm.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

import java.util.List;

@Configuration
@EnableConfigurationProperties({
        ApplicationConfig.AppProperties.class
})
public class ApplicationConfig {

    @Bean
    @ConfigurationProperties(prefix = "app")
    public AppProperties appProperties() {
        return new AppProperties();
    }

    @Data
    @Validated
    public static class AppProperties {

        private String name = "BookShelf";
        private String version = "1.0.0";
        private String description = "Personal Book Management System";

        private Upload upload = new Upload();
        private Pagination pagination = new Pagination();
        private Budget budget = new Budget();
        private Security security = new Security();
        private Cache cache = new Cache();

        @Data
        public static class Upload {
            private String dir = "uploads";
            private long maxFileSize = 10485760L; // 10MB
            private List<String> allowedTypes = List.of(
                    "image/jpeg", "image/jpg", "image/png", "image/gif"
            );
            private String coversDir = "covers";
        }

        @Data
        public static class Pagination {
            private int defaultPageSize = 12;
            private int maxPageSize = 100;
        }

        @Data
        public static class Budget {
            private long minAmount = 1000L;
            private String currency = "VND";
        }

        @Data
        public static class Security {
            private int sessionTimeout = 3600;
            private int rememberMeTimeout = 604800;
            private boolean debug = false;
        }

        @Data
        public static class Cache {
            private int ttl = 3600;
            private int maxEntries = 1000;
        }
    }
}
