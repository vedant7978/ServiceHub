package com.dalhousie.servicehub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Added this file to add localhost of developers to allowed origins.
 * Without this file you might receive error regarding CORS policy blocking your request when calling any API
 * [TODO]: At the end of the project when we have fully deployed, remove this file
 */
@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurator() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("Origin", "X-Requested-With", "Content-Type", "Accept", "Authorization")
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }
}
