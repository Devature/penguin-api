package dev.devature.penguin_api.controller;

import dev.devature.penguin_api.entity.User;
import dev.devature.penguin_api.service.RegisterService;
import dev.devature.penguin_api.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
     * @param user Take in a {@code User} object for the processing.
     * @return ResponseEntity of 201 if the registration was successful or 400 if the registration was
     * a failure.
     */
    @PostMapping("/registration")
    public ResponseEntity<String> register(@RequestBody User user){
        ApiResponse<User> newUser = registerService.registerUser(user);
        return ResponseEntity.status(newUser.getStatusCode()).body(newUser.getMessage());
    }
}
