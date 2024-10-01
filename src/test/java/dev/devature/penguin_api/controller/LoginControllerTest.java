package dev.devature.penguin_api.controller;

import dev.devature.penguin_api.entity.User;
import dev.devature.penguin_api.service.LoginService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private LoginService loginService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loginController = new LoginController(loginService);
    }

    @Test
    void testLogin_Successful() {
        User user = new User("agoodtestemail@testemail.com","Th1sisvalidpass!");

        when(loginService.checkValidity(user)).thenReturn(true);
        when(loginService.authenticate(user)).thenReturn(true);

        ResponseEntity<String> response = loginController.login(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Login successful", response.getBody());
    }


    @Test
    void testLogin_InvalidEmail() {
        User user = new User("testemail2testemail.com", "aValidPa55_");

        when(loginService.checkValidity(user)).thenReturn(true);
        when(loginService.authenticate(user)).thenReturn(false);

        ResponseEntity<String> response = loginController.login(user);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
    }

    @Test
    void testLogin_InvalidPass() {
        User user = new User("testemail@testemail.com","invalid");

        when(loginService.checkValidity(user)).thenReturn(true);
        when(loginService.authenticate(user)).thenReturn(false);

        ResponseEntity<String> response = loginController.login(user);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
    }
}
