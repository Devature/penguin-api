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
            throw new IllegalArgumentException("Memory and iteration must be above a positive value.");
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
     * Used to generate secured salt for password.
     * @return {@code String} with the encoded salt.
     */
    public String generateNewSalt(){
        byte[] salt = new byte[saltLength];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Do not use this is just a test.
     * @param subject Take in a name of the user to produce a session token.
     * @return A {@code String} token that the user will use.
     */
    public String createJWTToken(String issuer, String subject, Long id){
        SecretKey key = Jwts.SIG.HS256.key().build();

        return  Jwts.builder()
                .issuer(issuer)
                .subject(subject)
                .issuedAt(new Date())
                .id(id.toString())
                .signWith(key)
                .compact();
    }
}
