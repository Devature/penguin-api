package dev.devature.penguin_api.service;

import dev.devature.penguin_api.entity.User;
import dev.devature.penguin_api.model.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.devature.penguin_api.repository.UserRepository;
import dev.devature.penguin_api.utils.EmailPasswordValidationUtils;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     *
     * @param userRepository for grabbing password from database
     * @param passwordEncoder an Argon2PasswordEncoder used for verifying input password
     *                        against the hashed password in data
     */
    @Autowired
    public LoginService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
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

    public boolean checkValidity(User user) {
        return EmailPasswordValidationUtils.isValidEmail(user.getEmail())
                && EmailPasswordValidationUtils.isValidPassword(user.getPassword());
    }

    /**
     * @param user an User containing the input email and password
     * @return A {@code JwtToken} object containing the token the user should use
     *         for subsequent authentication requests, or null if the user did not
     *         authenticate successfully
     */
    public JwtToken authenticate(User user) {
        if (!checkValidity(user)) return null;
        User foundUser = userRepository.findByEmail(user.getEmail());
        if (foundUser != null && this.verifyPassword(user.getPassword(), foundUser.getPassword()))
            return this.jwtService.generateToken(user.getEmail());
        else return null;
    }
}
