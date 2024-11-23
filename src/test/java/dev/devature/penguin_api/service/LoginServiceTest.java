package dev.devature.penguin_api.service;

import dev.devature.penguin_api.entity.AppUser;
import dev.devature.penguin_api.repository.AppUserRepository;
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
    private AppUserRepository appUserRepository;

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
        AppUser appUser = new AppUser("test@example.com", "ValidP@ssw0rd!");
        String hashedPassword = "$argon2id$v=19$m=65536,t=3,p=4$...";

        AppUser mockAppUser = new AppUser("test@example.com", hashedPassword);
        when(appUserRepository.findByEmail(appUser.getEmail())).thenReturn(mockAppUser);
        when(passwordEncoder.matches(appUser.getPassword(), hashedPassword)).thenReturn(true);
        when(jwtService.generateToken(mockAppUser.getEmail())).thenReturn(new JwtToken("abc123"));

        JwtToken result = loginService.authenticate(appUser);

        assertNotNull(result);
        verify(appUserRepository).findByEmail(appUser.getEmail());
        verify(passwordEncoder).matches(appUser.getPassword(), hashedPassword);
    }

    @Test
    void testAuthenticate_Failure_InvalidPassword() {
        AppUser appUser = new AppUser("test@example.com", "invalidP@ssw0rd!");
        String hashedPassword = "$argon2id$v=19$m=65536,t=3,p=4$...";

        AppUser mockAppUser = new AppUser("test@example.com", hashedPassword);
        when(appUserRepository.findByEmail(appUser.getEmail())).thenReturn(mockAppUser);
        when(passwordEncoder.matches(appUser.getPassword(), hashedPassword)).thenReturn(false);

        JwtToken result = loginService.authenticate(appUser);

        assertNull(result);
        verify(appUserRepository).findByEmail(appUser.getEmail());
        verify(passwordEncoder).matches(appUser.getPassword(), hashedPassword);
    }

    @Test
    void testAuthenticate_Failure_UserNotFound() {
        AppUser appUser = new AppUser("notfound@example.com", "SomeP@ssw0rd!");

        when(appUserRepository.findByEmail(appUser.getEmail())).thenReturn(null);

        JwtToken result = loginService.authenticate(appUser);

        assertNull(result);
        verify(appUserRepository).findByEmail(appUser.getEmail());
    }

}