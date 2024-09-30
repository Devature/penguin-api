package dev.devature.penguin_api.service;

import dev.devature.penguin_api.dto.AuthRequest;
import dev.devature.penguin_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class EmailAuthServiceTest {

    @InjectMocks
    private EmailAuthService emailAuthService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Argon2PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testVerifyPassword_Success() {
        String plainPassword = "ValidP@ssw0rd!";
        String hashedPassword = "$argon2id$v=19$m=65536,t=3,p=4$...";

        // Assuming passwordEncoder matches the plain password with the hash
        when(passwordEncoder.matches(plainPassword, hashedPassword)).thenReturn(true);

        boolean result = emailAuthService.verifyPassword(plainPassword, hashedPassword);

        assertTrue(result);
        verify(passwordEncoder).matches(plainPassword, hashedPassword);
    }

    @Test
    void testVerifyPassword_Failure() {
        String plainPassword = "InvalidP@ss";
        String hashedPassword = "$argon2id$v=19$m=65536,t=3,p=4$...";

        when(passwordEncoder.matches(plainPassword, hashedPassword)).thenReturn(false);

        boolean result = emailAuthService.verifyPassword(plainPassword, hashedPassword);

        assertFalse(result);
        verify(passwordEncoder).matches(plainPassword, hashedPassword);
    }

    @Test
    void testCheckValidity_ValidEmailAndPassword() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("test@example.com");
        authRequest.setPassword("ValidP@ssw0rd!");

        boolean isValid = emailAuthService.checkValidity(authRequest);

        assertTrue(isValid);
    }

    @Test
    void testCheckValidity_InvalidEmail() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("invalid-email");
        authRequest.setPassword("ValidP@ssw0rd!");

        boolean isValid = emailAuthService.checkValidity(authRequest);

        assertFalse(isValid);
    }

    @Test
    void testCheckValidity_InvalidPassword() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("test@example.com");
        authRequest.setPassword("short");

        boolean isValid = emailAuthService.checkValidity(authRequest);

        assertFalse(isValid);
    }

    @Test
    void testAuthenticate_Success() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("test@example.com");
        authRequest.setPassword("ValidP@ssw0rd!");

        String hashedPassword = "$argon2id$v=19$m=65536,t=3,p=4$...";

        when(userRepository.getHashedPasswordByEmail(authRequest.getEmail())).thenReturn(hashedPassword);
        when(passwordEncoder.matches(authRequest.getPassword(), hashedPassword)).thenReturn(true);

        boolean result = emailAuthService.authenticate(authRequest);

        assertTrue(result);
        verify(userRepository).getHashedPasswordByEmail(authRequest.getEmail());
    }

    @Test
    void testAuthenticate_Failure_InvalidPassword() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("test@example.com");
        authRequest.setPassword("WrongPassword");

        String hashedPassword = "$argon2id$v=19$m=65536,t=3,p=4$...";

        when(userRepository.getHashedPasswordByEmail(authRequest.getEmail())).thenReturn(hashedPassword);
        when(passwordEncoder.matches(authRequest.getPassword(), hashedPassword)).thenReturn(false);

        boolean result = emailAuthService.authenticate(authRequest);

        assertFalse(result);
    }

    @Test
    void testAuthenticate_Failure_UserNotFound() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("notfound@example.com");
        authRequest.setPassword("SomePassword");

        when(userRepository.getHashedPasswordByEmail(authRequest.getEmail())).thenReturn(null);

        boolean result = emailAuthService.authenticate(authRequest);

        assertFalse(result);
    }
}
