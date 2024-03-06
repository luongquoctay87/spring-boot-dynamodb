package com.sample.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Value("${cors.allow.origins}")
    private String allowOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowOrigins)
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE") // Allowed HTTP methods
                .allowedHeaders("*") // Allowed request headers
                .allowCredentials(false)
                .maxAge(3600);
    }
}
