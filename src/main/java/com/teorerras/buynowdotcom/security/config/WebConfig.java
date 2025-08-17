package com.teorerras.buynowdotcom.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // all paths
                .allowedOrigins("http://localhost:5174") // React app's URL
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // methods you want to allow
                .allowedHeaders("*");
    }
}

