package dev.devature.penguin_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.devature.penguin_api.interfaces.AuthenticationStrategy;
import dev.devature.penguin_api.repository.UserRepository;
import dev.devature.penguin_api.dto.AuthRequest;
import dev.devature.penguin_api.utils.ValidationUtils;

@Service
public class EmailAuthService implements AuthenticationStrategy {

    @Autowired
    private final UserRepository userRepository;
    private final Argon2PasswordEncoder passwordEncoder;

    /**
     *
     * @param userRepository for grabbing password from database
     * @param passwordEncoder an Argon2PasswordEncoder used for verifying input password
     *                        against the hashed password in data
     */
    @Autowired
    public EmailAuthService(UserRepository userRepository, Argon2PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     *
     * @param plainPassword the password input by user
     * @param hashedPassword the stored password hash to check against
     * @return true if the password matches, false otherwise
     */
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        // Argon2PasswordEncoder handles this automatically
        return passwordEncoder.matches(plainPassword, hashedPassword);
    }

    public boolean checkValidity(AuthRequest authRequest) {
        return ValidationUtils.isValidEmail(authRequest.getEmail())
                && ValidationUtils.isValidPassword(authRequest.getPassword());
    }

    /**
     * @param authRequest an AuthRequest containing the authentication type
     *                   and, in this case, the input email and password
     * @return true if the password is verified successfully, false otherwise
     */
    @Override
    public boolean authenticate(AuthRequest authRequest) {
        String hashedPassword = userRepository.getHashedPasswordByEmail(authRequest.getEmail());
        return hashedPassword != null && this.verifyPassword(authRequest.getPassword(), hashedPassword);
    }
}
