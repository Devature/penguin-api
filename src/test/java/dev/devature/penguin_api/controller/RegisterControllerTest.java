package dev.devature.penguin_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.devature.penguin_api.entity.User;
import dev.devature.penguin_api.service.RegisterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegisterService registerService;

    private ObjectMapper objectMapper;

    public RegisterControllerTest(){
        objectMapper = new ObjectMapper();
    }

    /**
     * Sending a mock http request to POST localhost:8080/api/v1/users/registration with valid credentials
     *
     *  Expected Response:
     *  Status Code: 201
     *  Response Body: Registration successful.
     *  /TODO: Switch this to session token.
     */
    @Test
    public void registerUserSucceed() throws Exception {
        User user = new User("johnsmith@example.com", "Password_1");
        String userJson = objectMapper.writeValueAsString(user);

        when(registerService.registerUser(user)).thenReturn(user);

        this.mockMvc.perform(post("/api/v1/user/registration")
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().is(201))
                .andExpect(content()
                        .string(containsString("Registration successful.")));
    }

    /**
     * Sending a mock http request to POST localhost:8080/api/v1/users/registration with valid credentials
     *
     *  Expected Response:
     *  Status Code: 400
     *  Response Body: Registration unsuccessful. Failed to create an account.
     *  /TODO: Switch this to session token.
     */
    @Test
    public void registerUserFailed() throws Exception {
        User user = new User("johnsmith@example.com", "Password_1");
        String userJson = objectMapper.writeValueAsString(user);

        when(registerService.registerUser(user)).thenReturn(null);

        this.mockMvc.perform(post("/api/v1/user/registration")
                .contentType("application/json")
                .content(userJson))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .string(containsString("Registration unsuccessful. Failed to create an account.")));
    }
}
