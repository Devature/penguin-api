package dev.devature.penguin_api.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.devature.penguin_api.interfaces.AuthenticationStrategy;
import dev.devature.penguin_api.repository.AuthenticationRepository;
import dev.devature.penguin_api.dto.AuthRequest;

import java.util.regex.Pattern;

@Service
public class EmailAuthService implements AuthenticationStrategy {

    private final String passwordRegex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[~`!@#$%^&*()\\-_+={}\\[\\]|;:<>,./?]).{8,}$";
    private final String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    private final Pattern passPattern;
    private final Pattern emailPattern;

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
    public EmailAuthService(AuthenticationRepository authenticationRepository, Argon2PasswordEncoder passwordEncoder) {
        this.authenticationRepository = authenticationRepository;
        this.passwordEncoder = passwordEncoder;

        passPattern = Pattern.compile(passwordRegex);
        emailPattern = Pattern.compile(emailRegex);
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

    public boolean checkValidity(AuthRequest authRequest) {
        return emailPattern.matcher(authRequest.getEmail()).matches()
                && passPattern.matcher(authRequest.getPassword()).matches();
    }

    /**
     * @param authRequest an AuthRequest containing the authentication type
     *                   and, in this case, the input username and password
     * @return true if the password is verified successfully, false otherwise
     */
    @Override
    public boolean authenticate(AuthRequest authRequest) {
        String hashedPassword = authenticationRepository.getHashedPasswordByEmail(authRequest.getEmail());
        return hashedPassword != null && this.verifyPassword(authRequest.getPassword(), hashedPassword);
    }
}
