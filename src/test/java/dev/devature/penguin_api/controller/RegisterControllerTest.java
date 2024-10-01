package dev.devature.penguin_api.controller;

import dev.devature.penguin_api.entity.User;
import dev.devature.penguin_api.service.RegisterService;

import dev.devature.penguin_api.utils.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

public class RegisterControllerTest extends RequestsTest {
    @MockBean
    private RegisterService registerService;

    @Test
    public void registerUserSucceed() throws Exception {
        User user = new User("johnsmith@example.com", "Password_1");
        String userJson = objectMapper.writeValueAsString(user);

        when(registerService.checkEmailAvailable(user.getEmail())).thenReturn(true);

        ApiResponse<User> userResponse = new ApiResponse<>(HttpStatus.CREATED, "Registration successful.", user);

        when(registerService.registerUser(user)).thenReturn(userResponse);

        this.mockMvc.perform(post("/api/v1/user/registration")
                        .with(csrf())
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().is(201))
                .andExpect(content()
                        .string(containsString("Registration successful.")))
                .andDo(document("registration/success"));
    }

    @Test
    public void registerUser_WithFailed() throws Exception {
        User user = new User("johnsmith@example.com", "Password_1");
        String userJson = objectMapper.writeValueAsString(user);

        when(registerService.checkEmailAvailable(user.getEmail())).thenReturn(true);

        // Mocking the registerUser to return a failed ApiResponse
        ApiResponse<User> userResponse = new ApiResponse<>(HttpStatus.BAD_REQUEST,
                "Registration unsuccessful. Failed to create an account.", null);
        when(registerService.registerUser(user)).thenReturn(userResponse);

        this.mockMvc.perform(post("/api/v1/user/registration")
                        .with(csrf())
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .string(containsString("Registration unsuccessful. Failed to create an account.")))
                .andDo(document("registration/failure"));
    }

    @Test
    public void registerUser_WithEmailConflict() throws Exception {
        User user = new User("testsmith@example.com", "Password_1");
        String userJson = objectMapper.writeValueAsString(user);

        // TODO: Use H2 for this test since we need a stateful testing. Mock is stateless. This is useless for now.

        when(registerService.checkEmailAvailable(user.getEmail())).thenReturn(false);

        ApiResponse<User> userResponse = new ApiResponse<>(HttpStatus.CONFLICT,
                "Someone is already using that email", user);
        when(registerService.registerUser(user)).thenReturn(userResponse);

        this.mockMvc.perform(post("/api/v1/user/registration")
                        .with(csrf())
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().isConflict())
                .andExpect(content()
                        .string(containsString("Someone is already using that email")))
                .andDo(document("registration/failure"));
    }
}
