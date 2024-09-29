package dev.devature.penguin_api.service;

import dev.devature.penguin_api.config.SecurityConfig;
import dev.devature.penguin_api.entity.User;
import dev.devature.penguin_api.repository.RegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@Transactional
public class RegisterService {
    private final RegisterRepository registerRepository;
    private final SecurityConfig securityConfig;
    private final String passwordRegex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[~`!@#$%^&*()\\-_+={}\\[\\]|;:<>,./?]).{8,}$";
    private final String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    private final Pattern passPattern;
    private final Pattern emailPattern;

    @Autowired
    public RegisterService(RegisterRepository registerRepository, SecurityConfig securityConfig) {
        this.registerRepository = registerRepository;
        this.securityConfig = securityConfig;

        passPattern = Pattern.compile(passwordRegex);
        emailPattern = Pattern.compile(emailRegex);
    }

    /**
     * @param user Take in a {@code User} object to be processed.
     * @return User {@code User} object if the account was successfully add or
     * {@code null} if it failed to add or meet requirements.
     */
    public User registerUser(User user){
        boolean isEmailValid = checkEmailRequirements(user.getEmail());
        boolean isPasswordValid = checkPasswordRequirements(user.getPassword());
        boolean isEmailAvailable = checkEmailAvailable(user.getEmail());

        if(!isEmailValid || !isPasswordValid || !isEmailAvailable){
            return null;
        }

        String salt = securityConfig.generateNewSalt();
        user.setSalt(salt);

        String saltedPassword = user.getSalt() + user.getPassword();

        String hashedPassword = securityConfig.passwordEncoder().encode(saltedPassword);
        user.setPassword(hashedPassword);

        return registerRepository.save(user);
    }

    /**
     * This is based on the RFC 5322 requirements for email.
     * @param email Takes in a  {@code String} email to be processed.
     * @return A {@code True} if the password meets requirements or {@code False} if it fails to
     * meet the requirements.
     */
    public boolean checkEmailRequirements(String email){
        return emailPattern.matcher(email).matches();
    }

    /**
     * This is based on 1 uppercase, 1 lowercase, 1 special, 1 number, and 8 characters or greater.
     * @param password Take in {@code String} object password.
     * @return A {@code True} if the password meets requirements or {@code False} if it fails to
     * meet the requirements.
     */
    public boolean checkPasswordRequirements(String password){
        return passPattern.matcher(password).matches();
    }

    /**
     * @param email Take in a {@code String} to check if email already exist in database.
     * @return Return {@code True} if email does not available or {@code False} if the email is unavailable.
     */
    public boolean checkEmailAvailable(String email){
        User user = registerRepository.findByEmail(email);
        return user == null;
    }
}
