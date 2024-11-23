package dev.devature.penguin_api.service;

import dev.devature.penguin_api.entity.AppUser;
import dev.devature.penguin_api.model.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.devature.penguin_api.repository.AppUserRepository;
import dev.devature.penguin_api.utils.EmailPasswordValidationUtils;

@Service
public class LoginService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     *
     * @param appUserRepository for grabbing password from database
     * @param passwordEncoder an Argon2PasswordEncoder used for verifying input password
     *                        against the hashed password in data
     */
    @Autowired
    public LoginService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.appUserRepository = appUserRepository;
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

    public boolean checkValidity(AppUser appUser) {
        return EmailPasswordValidationUtils.isValidEmail(appUser.getEmail())
                && EmailPasswordValidationUtils.isValidPassword(appUser.getPassword());
    }

    /**
     * @param appUser an User containing the input email and password
     * @return A {@code JwtToken} object containing the token the user should use
     *         for subsequent authentication requests, or null if the user did not
     *         authenticate successfully
     */
    public JwtToken authenticate(AppUser appUser) {
        if (!checkValidity(appUser)) return null;
        AppUser foundAppUser = appUserRepository.findByEmail(appUser.getEmail());
        if (foundAppUser != null && this.verifyPassword(appUser.getPassword(), foundAppUser.getPassword()))
            return this.jwtService.generateToken(appUser.getEmail());
        else return null;
    }
}
