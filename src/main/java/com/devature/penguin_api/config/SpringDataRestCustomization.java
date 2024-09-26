package com.devature.penguin_api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

// Spring Data REST CORS configuration
@Component
public class SpringDataRestCustomization implements RepositoryRestConfigurer {
    @Autowired
    Environment environment;

    @Override
    public void configureRepositoryRestConfiguration(
            RepositoryRestConfiguration config,
            CorsRegistry registry) {

        String clientOrigin = environment.getProperty("clientOrigin");

        registry.addMapping("/**")
						.allowedOrigins(clientOrigin)
						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
						.allowCredentials(true);
    }
}