package dev.devature.penguin_api.config;

import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${argon2.saltLength}")
    private int saltLength;

    @Value("${argon2.hashLength}")
    private int hashLength;

    @Value("${argon2.parallelism}")
    private int parallelism;

    @Value("${argon2.memory}")
    private int memory;

    @Value("${argon2.iterations}")
    private int iterations;

    private final SecureRandom random = new SecureRandom();

    /**
     * Checked if the configuration are correct and won't cause issue when encoding.
     */
    @PostConstruct
    public void validateConfig(){
        if(memory <= 0 || iterations <= 0 || saltLength <= 0 || hashLength <= 0 || parallelism <= 0){
            throw new IllegalArgumentException("All values must be a positive value.");
        }
    }

    /**
     * Used to encode the user's password.
     * @return Returns a new Argon2 password encoders with all the preset.
     */
    @Bean
    public Argon2PasswordEncoder passwordEncoder(){
        return new Argon2PasswordEncoder(saltLength, hashLength, parallelism, memory, iterations);
    }


    /**
     * Builds the secret key to be used for signing and verifying JWT tokens.
     * @return A {@code SecretKey} to be used for signing and verifying JWT tokens.
     */
    @Bean
    public SecretKey secretKey() {
        return Jwts.SIG.HS256.key().build();
    }
}
