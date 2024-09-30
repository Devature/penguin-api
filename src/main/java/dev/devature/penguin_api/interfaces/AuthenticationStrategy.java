package dev.devature.penguin_api.interfaces;

import dev.devature.penguin_api.dto.AuthRequest;

public interface AuthenticationStrategy {
    boolean checkValidity(AuthRequest authRequest);
    boolean authenticate(AuthRequest authRequest);
}