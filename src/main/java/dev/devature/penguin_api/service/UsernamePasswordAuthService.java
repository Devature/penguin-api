package dev.devature.penguin_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.devature.penguin_api.interfaces.AuthenticationStrategy;
import dev.devature.penguin_api.repository.AuthenticationRepository;
import dev.devature.penguin_api.dto.AuthRequest;

@Service
public class UsernamePasswordAuthService implements AuthenticationStrategy {

    @Autowired
    private final AuthenticationRepository authenticationRepository;
    private final Argon2PasswordEncoder passwordEncoder;

    /**
     *
     * @param authenticationRepository for grabbing password from database
     * @param passwordEncoder an Argon2PasswordEncoder used for verifying input password
     *                        against the hashed password in data
     */
    @Autowired
    public UsernamePasswordAuthService(AuthenticationRepository authenticationRepository, Argon2PasswordEncoder passwordEncoder) {
        this.authenticationRepository = authenticationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     *
     * @param plainPassword the password input by user
     * @param hashedPassword the stored password hash to check against
     * @return true if the password matches, false otherwise
     */
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        // does not need to hash the plainPassword manually. Argon2PasswordEncoder handles this automatically
        return passwordEncoder.matches(plainPassword, hashedPassword);
    }

    /**
     *
     * @param authRequest an AuthRequest containing the authentication type
     *                   and, in this case, the input username and password
     * @return true if the password is verified successfully, false otherwise
     */
    @Override
    public boolean authenticate(AuthRequest authRequest) {
        String hashedPassword = authenticationRepository.getHashedPasswordByUsername(authRequest.getUsername());
        return hashedPassword != null && this.verifyPassword(authRequest.getPassword(), hashedPassword);
    }
}
