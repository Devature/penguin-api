package dev.devature.penguin_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.devature.penguin_api.interfaces.AuthenticationStrategy;
import dev.devature.penguin_api.dto.AuthRequest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class AuthenticationController {

    private final Map<String, AuthenticationStrategy> strategies;

    // makes a map (like this Map<String, AuthenticationStrategy>) from our AuthenticationStrategy implementations
    @Autowired
    public AuthenticationController(List<AuthenticationStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(strategy -> strategy.getClass().getSimpleName(), strategy -> strategy));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest authRequest) {
        String authType = authRequest.getAuthType();
        AuthenticationStrategy strategy = strategies.get(authType + "AuthService");

        if (strategy == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unsupported authentication type");
        }

        boolean isAuthenticated = strategy.authenticate(authRequest);

        return isAuthenticated ? ResponseEntity.ok("Login successful") :
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }


}


