package dev.devature.penguin_api.service;

import dev.devature.penguin_api.config.SecurityConfig;
import dev.devature.penguin_api.entity.User;
import dev.devature.penguin_api.repository.UserRepository;
import dev.devature.penguin_api.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegisterService {
    private final UserRepository userRepository;
    private final SecurityConfig securityConfig;

    @Autowired
    public RegisterService(UserRepository userRepository, SecurityConfig securityConfig) {
        this.userRepository = userRepository;
        this.securityConfig = securityConfig;
    }

    /**
     * @param user Take in a {@code User} object to be processed.
     * @return User {@code User} object if the account was successfully added or
     * {@code null} if it failed to add or meet requirements.
     */
    public User registerUser(User user) {
        boolean isEmailValid = ValidationUtils.isValidEmail(user.getEmail());
        boolean isPasswordValid = ValidationUtils.isValidPassword(user.getPassword());
        boolean isEmailAvailable = checkEmailAvailable(user.getEmail());

        if (!isEmailValid || !isPasswordValid || !isEmailAvailable) {
            return null;
        }

        String salt = securityConfig.generateNewSalt();
        user.setSalt(salt);

        String saltedPassword = user.getSalt() + user.getPassword();
        String hashedPassword = securityConfig.passwordEncoder().encode(saltedPassword);
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }

    /**
     * @param email Take in a {@code String} to check if the email already exists in the database.
     * @return Return {@code True} if email is not available or {@code False} if the email is available.
     */
    public boolean checkEmailAvailable(String email) {
        User user = userRepository.findByEmail(email);
        return user == null;
    }
}
