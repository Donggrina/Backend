package com.codeit.donggrina.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {
    private final String ALLOWED_METHODS = "GET,POST,PUT,DELETE,OPTIONS";
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {

        corsRegistry.addMapping("/**")
            .exposedHeaders("Set-Cookie")
            .allowedOrigins("https://ftontend.vercel.app")
            .allowedMethods(ALLOWED_METHODS.split(","))
            .allowCredentials(true);
    }
}
