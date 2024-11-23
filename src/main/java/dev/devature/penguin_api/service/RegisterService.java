package dev.devature.penguin_api.service;

import dev.devature.penguin_api.entity.AppUser;
import dev.devature.penguin_api.enums.RegisterResult;
import dev.devature.penguin_api.repository.AppUserRepository;
import dev.devature.penguin_api.utils.EmailPasswordValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegisterService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * @param appUser Take in a {@code User} object to be processed.
     * @return User {@code User} object if the account was successfully add or
     * {@code null} if it failed to add or meet requirements.
     */
    public RegisterResult registerUser(AppUser appUser){
        boolean isEmailValid = EmailPasswordValidationUtils.isValidEmail(appUser.getEmail());
        boolean isPasswordValid = EmailPasswordValidationUtils.isValidPassword(appUser.getPassword());
        boolean isEmailAvailable = checkEmailAvailable(appUser.getEmail());

        if(!isEmailAvailable){
            return RegisterResult.EMAIL_TAKEN;
        }

        if(!isEmailValid || !isPasswordValid){
            return RegisterResult.INVALID_ACCOUNT_INFO;
        }

        String hashedPassword = this.passwordEncoder.encode(appUser.getPassword());
        appUser.setPassword(hashedPassword);

        AppUser dbAppUser = appUserRepository.save(appUser);

        boolean isValid = dbAppUser.getId() != null
                && dbAppUser.getId() >= 0
                && dbAppUser.getEmail() != null
                && !dbAppUser.getEmail().isEmpty()
                && dbAppUser.getPassword() != null
                && !dbAppUser.getPassword().isEmpty();

        return isValid ? RegisterResult.SUCCESS : RegisterResult.UNKNOWN_ERROR;
    }

    /**
     * @param email Take in a {@code String} to check if email already exist in database.
     * @return Return {@code True} if email does not available or {@code False} if the email is unavailable.
     */
    public boolean checkEmailAvailable(String email){
        AppUser appUser = appUserRepository.findByEmail(email);
        return appUser == null;
    }
}