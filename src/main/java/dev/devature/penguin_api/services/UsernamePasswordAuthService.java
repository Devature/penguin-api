package dev.devature.penguin_api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.devature.penguin_api.interfaces.AuthenticationStrategy;
import dev.devature.penguin_api.dto.AuthRequest;
import dev.devature.penguin_api.dao.UserDAO;

@Service
public class UsernamePasswordAuthService implements AuthenticationStrategy {

    private final UserDAO userDAO;
    private final Argon2PasswordEncoder passwordEncoder;

    @Autowired
    public UsernamePasswordAuthService(UserDAO userDAO, Argon2PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        // does not need to hash the plainPassword manually. Argon2PasswordEncoder handles this automatically
        return passwordEncoder.matches(plainPassword, hashedPassword);
    }

    @Override
    public boolean authenticate(AuthRequest authRequest) {
        String hashedPassword = userDAO.getHashedPasswordByUsername(authRequest.getUsername());
        return hashedPassword != null && this.verifyPassword(authRequest.getPassword(), hashedPassword);
    }
}
