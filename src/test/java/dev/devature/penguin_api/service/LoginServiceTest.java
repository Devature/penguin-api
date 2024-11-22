package dev.devature.penguin_api.service;

import dev.devature.penguin_api.entity.Users;
import dev.devature.penguin_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import dev.devature.penguin_api.model.JwtToken;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Argon2PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;


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
        Users users = new Users("test@example.com", "ValidP@ssw0rd!");
        String hashedPassword = "$argon2id$v=19$m=65536,t=3,p=4$...";

        Users mockUsers = new Users("test@example.com", hashedPassword);
        when(userRepository.findByEmail(users.getEmail())).thenReturn(mockUsers);
        when(passwordEncoder.matches(users.getPassword(), hashedPassword)).thenReturn(true);
        when(jwtService.generateToken(mockUsers.getEmail())).thenReturn(new JwtToken("abc123"));

        JwtToken result = loginService.authenticate(users);

        assertNotNull(result);
        verify(userRepository).findByEmail(users.getEmail());
        verify(passwordEncoder).matches(users.getPassword(), hashedPassword);
    }

    @Test
    void testAuthenticate_Failure_InvalidPassword() {
        Users users = new Users("test@example.com", "invalidP@ssw0rd!");
        String hashedPassword = "$argon2id$v=19$m=65536,t=3,p=4$...";

        Users mockUsers = new Users("test@example.com", hashedPassword);
        when(userRepository.findByEmail(users.getEmail())).thenReturn(mockUsers);
        when(passwordEncoder.matches(users.getPassword(), hashedPassword)).thenReturn(false);

        JwtToken result = loginService.authenticate(users);

        assertNull(result);
        verify(userRepository).findByEmail(users.getEmail());
        verify(passwordEncoder).matches(users.getPassword(), hashedPassword);
    }

    @Test
    void testAuthenticate_Failure_UserNotFound() {
        Users users = new Users("notfound@example.com", "SomeP@ssw0rd!");

        when(userRepository.findByEmail(users.getEmail())).thenReturn(null);

        JwtToken result = loginService.authenticate(users);

        assertNull(result);
        verify(userRepository).findByEmail(users.getEmail());
    }

}