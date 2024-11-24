package dev.devature.penguin_api.config;

import dev.devature.penguin_api.interceptor.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ServerConfiguration implements WebMvcConfigurer {
    private final Environment env;
    private final AuthenticationInterceptor authInterceptor;

    public ServerConfiguration(Environment env, AuthenticationInterceptor authInterceptor) {
        this.env = env;
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String clientOrigin = this.env.getProperty("clientOrigin");
        String clientOrigin2 = this.env.getProperty("clientOrigin2");

        registry.addMapping("/**")
                .allowedOrigins(
                        clientOrigin,
                        clientOrigin2
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor);
    }
}
