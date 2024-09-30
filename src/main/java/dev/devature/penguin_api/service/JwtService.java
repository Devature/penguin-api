package dev.devature.penguin_api.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
public class JwtService {
    private final SecretKey secretKey;
    private final JwtParser jwtParser;

    @Value("${jwt.defaultTTLSeconds}")
    private int defaultTTLSeconds;

    @Autowired
    public JwtService(SecretKey secretKey) {
        this.secretKey = secretKey;
        this.jwtParser = Jwts.parser().verifyWith(secretKey).build();
    }

    public Optional<Claims> verifyToken(String token) {
        try {
            return Optional.of(this.jwtParser.parseSignedClaims(token).getPayload());
        } catch (JwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public String generateToken(String subject) {
        return this.generateToken(subject, null);
    }

    public String generateToken(String subject, Map<String, ?> claims) {
        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .issuer("Ticket Penguin")
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(defaultTTLSeconds)))
                .signWith(this.secretKey)
                .compact();
    }
}
