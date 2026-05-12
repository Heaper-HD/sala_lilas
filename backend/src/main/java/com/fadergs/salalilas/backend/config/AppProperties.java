package com.fadergs.salalilas.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "app")
@Data
public class AppProperties {
    private Jwt jwt = new Jwt();
    private Frontend frontend = new Frontend();
    private Pdf pdf = new Pdf();

    @Data
    public static class Jwt {
        private String secret;
        private long accessTokenExpiration;
        private long refreshTokenExpiration;
    }

    @Data
    public static class Frontend {
        private String callbackUrl;
    }

    @Data
    public static class Pdf {
        private String storagePath;
    }
}
