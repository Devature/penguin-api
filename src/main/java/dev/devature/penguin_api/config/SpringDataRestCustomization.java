package dev.devature.penguin_api.config;

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
        String clientOrigin2 = environment.getProperty("clientOrigin2");

        registry.addMapping("/**")
						.allowedOrigins(
                                clientOrigin,
                                clientOrigin2
                        )
						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
						.allowCredentials(true);
    }
}