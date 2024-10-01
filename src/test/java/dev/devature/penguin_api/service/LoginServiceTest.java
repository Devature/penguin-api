package dev.devature.penguin_api.service;

import dev.devature.penguin_api.entity.User;
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

class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

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

        when(passwordEncoder.matches(plainPassword, hashedPassword)).thenReturn(true);

        boolean result = loginService.verifyPassword(plainPassword, hashedPassword);

        assertTrue(result);
        verify(passwordEncoder).matches(plainPassword, hashedPassword);
    }

    @Test
    void testAuthenticate_Success() {
        User user = new User("test@example.com", "ValidP@ssw0rd!");
        String hashedPassword = "$argon2id$v=19$m=65536,t=3,p=4$...";

        User mockUser = new User("test@example.com", hashedPassword);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(mockUser);

        when(passwordEncoder.matches(user.getPassword(), hashedPassword)).thenReturn(true);

        boolean result = loginService.authenticate(user);

        assertTrue(result);
        verify(userRepository).findByEmail(user.getEmail());
        verify(passwordEncoder).matches(user.getPassword(), hashedPassword);
    }

    @Test
    void testAuthenticate_Failure_InvalidPassword() {
        User user = new User("test@example.com", "invalid");
        String hashedPassword = "$argon2id$v=19$m=65536,t=3,p=4$...";

        User mockUser = new User("test@example.com", hashedPassword);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(mockUser);
        when(passwordEncoder.matches(user.getPassword(), hashedPassword)).thenReturn(false);

        boolean result = loginService.authenticate(user);

        assertFalse(result);
        verify(userRepository).findByEmail(user.getEmail());
        verify(passwordEncoder).matches(user.getPassword(), hashedPassword);
    }



    @Test
    void testAuthenticate_Failure_UserNotFound() {
        User user = new User("notfound@example.com", "SomePassword");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);

        boolean result = loginService.authenticate(user);

        assertFalse(result);
        verify(userRepository).findByEmail(user.getEmail());
    }

}