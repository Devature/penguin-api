package dev.devature.penguin_api.controller;

import dev.devature.penguin_api.entity.AppUser;
import dev.devature.penguin_api.enums.RegisterResult;
import dev.devature.penguin_api.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class RegisterController {
    private final RegisterService registerService;

    @Autowired
    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    /**
     * @param appUser Take in a {@code User} object for the processing.
     * @return ResponseEntity of 201 if the registration was successful or 400 if the registration was
     * a failure.
     */
    @PostMapping("/registration")
    public ResponseEntity<String> register(@RequestBody AppUser appUser){
        RegisterResult registerServiceStatus = registerService.registerUser(appUser);

        switch (registerServiceStatus){
            case SUCCESS -> {
                return ResponseEntity.status(HttpStatus.CREATED).body("Registration successful.");
            }
            case EMAIL_TAKEN -> {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Someone is already using that email.");
            }
            case INVALID_ACCOUNT_INFO -> {
                return ResponseEntity.badRequest().body("Account information is invalid.");
            }
            default -> {
                return ResponseEntity.status(422)
                        .body("Invalid registration data. Please review your input and try again.");
            }
        }
    }
}
