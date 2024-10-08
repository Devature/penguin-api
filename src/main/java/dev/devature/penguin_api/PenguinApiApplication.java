package dev.devature.penguin_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class PenguinApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PenguinApiApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer(Environment environment) {
		String clientOrigin = environment.getProperty("clientOrigin");

		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins(clientOrigin)
						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
						.allowCredentials(true);
			}
		};
	}

}
