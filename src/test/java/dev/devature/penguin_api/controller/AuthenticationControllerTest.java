package dev.devature.penguin_api.controller;

import dev.devature.penguin_api.dto.AuthRequest;
import dev.devature.penguin_api.interfaces.AuthenticationStrategy;
import dev.devature.penguin_api.service.EmailAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private EmailAuthService emailAuthService;

    @Mock
    private List<AuthenticationStrategy> strategyList;

    private Map<String, AuthenticationStrategy> strategies;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        strategies = new HashMap<>();
        strategies.put("EmailAuthService", emailAuthService);
        authenticationController = new AuthenticationController(List.of(emailAuthService));
    }

    @Test
    void testLogin_Successful() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setAuthType("Email");
        authRequest.setEmail("agoodtestemail@testemail.com");
        authRequest.setPassword("Th1sisvalidpass!");

        when(emailAuthService.checkValidity(authRequest)).thenReturn(true);
        when(emailAuthService.authenticate(authRequest)).thenReturn(true);

        ResponseEntity<String> response = authenticationController.login(authRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Login successful", response.getBody());
    }

    @Test
    void testLogin_UnsupportedAuthType() {
        AuthRequest authRequest = new AuthRequest(
                "Unsupported",
                  "testing1@validemail.com",
               "Th1sisvalidpass!"
        );

        ResponseEntity<String> response = authenticationController.login(authRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Unsupported authentication type", response.getBody());
    }

    @Test
    void testLogin_InvalidEmail() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setAuthType("Email");
        authRequest.setEmail("testemail2testemail.com");
        authRequest.setPassword("aValidPa55_");

        when(emailAuthService.checkValidity(authRequest)).thenReturn(true);
        when(emailAuthService.authenticate(authRequest)).thenReturn(false);

        ResponseEntity<String> response = authenticationController.login(authRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
    }

    @Test
    void testLogin_InvalidPass() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setAuthType("Email");
        authRequest.setEmail("testemail@testemail.com");
        authRequest.setPassword("invalid");

        when(emailAuthService.checkValidity(authRequest)).thenReturn(true);
        when(emailAuthService.authenticate(authRequest)).thenReturn(false);

        ResponseEntity<String> response = authenticationController.login(authRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
    }
}
