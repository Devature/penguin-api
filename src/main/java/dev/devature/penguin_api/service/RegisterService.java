package dev.devature.penguin_api.service;

import dev.devature.penguin_api.entity.User;
import dev.devature.penguin_api.repository.UserRepository;
import dev.devature.penguin_api.utils.EmailPasswordValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegisterService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * @param user Take in a {@code User} object to be processed.
     * @return User {@code User} object if the account was successfully add or
     * {@code null} if it failed to add or meet requirements.
     */
    public User registerUser(User user){
        boolean isEmailValid = EmailPasswordValidationUtils.isValidEmail(user.getEmail());
        boolean isPasswordValid = EmailPasswordValidationUtils.isValidPassword(user.getPassword());
        boolean isEmailAvailable = checkEmailAvailable(user.getEmail());

        if(!isEmailValid || !isPasswordValid || !isEmailAvailable){
            return null;
        }

        String hashedPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }

    /**
     * @param email Take in a {@code String} to check if email already exist in database.
     * @return Return {@code True} if email does not available or {@code False} if the email is unavailable.
     */
    public boolean checkEmailAvailable(String email){
        User user = userRepository.findByEmail(email);
        return user == null;
    }
}