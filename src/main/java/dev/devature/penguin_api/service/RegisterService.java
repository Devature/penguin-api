package dev.devature.penguin_api.service;

import dev.devature.penguin_api.entity.Users;
import dev.devature.penguin_api.enums.RegisterResult;
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
     * @param users Take in a {@code User} object to be processed.
     * @return User {@code User} object if the account was successfully add or
     * {@code null} if it failed to add or meet requirements.
     */
    public RegisterResult registerUser(Users users){
        boolean isEmailValid = EmailPasswordValidationUtils.isValidEmail(users.getEmail());
        boolean isPasswordValid = EmailPasswordValidationUtils.isValidPassword(users.getPassword());
        boolean isEmailAvailable = checkEmailAvailable(users.getEmail());

        if(!isEmailAvailable){
            return RegisterResult.EMAIL_TAKEN;
        }

        if(!isEmailValid || !isPasswordValid){
            return RegisterResult.INVALID_ACCOUNT_INFO;
        }

        String hashedPassword = this.passwordEncoder.encode(users.getPassword());
        users.setPassword(hashedPassword);

        Users dbUsers = userRepository.save(users);

        boolean isValid = dbUsers.getId() != null
                && dbUsers.getId() >= 0
                && dbUsers.getEmail() != null
                && !dbUsers.getEmail().isEmpty()
                && dbUsers.getPassword() != null
                && !dbUsers.getPassword().isEmpty();

        return isValid ? RegisterResult.SUCCESS : RegisterResult.UNKNOWN_ERROR;
    }

    /**
     * @param email Take in a {@code String} to check if email already exist in database.
     * @return Return {@code True} if email does not available or {@code False} if the email is unavailable.
     */
    public boolean checkEmailAvailable(String email){
        Users users = userRepository.findByEmail(email);
        return users == null;
    }
}