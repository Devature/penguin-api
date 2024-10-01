package dev.devature.penguin_api.service;

import dev.devature.penguin_api.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.devature.penguin_api.repository.UserRepository;
import dev.devature.penguin_api.utils.EmailPasswordValidationUtils;

@Service
public class LoginService {

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
    public LoginService(UserRepository userRepository, Argon2PasswordEncoder passwordEncoder) {
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

    public boolean checkValidity(User user) {
        return EmailPasswordValidationUtils.isValidEmail(user.getEmail())
                && EmailPasswordValidationUtils.isValidPassword(user.getPassword());
    }

    /**
     * @param user an User containing the input email and password
     * @return true if the password is verified successfully, false otherwise
     */
    public boolean authenticate(User user) {
        User foundUser = userRepository.findByEmail(user.getEmail());
        if (foundUser == null) return false;
        return foundUser.getPassword() != null &&  this.verifyPassword(user.getPassword(), foundUser.getPassword());
    }
}
