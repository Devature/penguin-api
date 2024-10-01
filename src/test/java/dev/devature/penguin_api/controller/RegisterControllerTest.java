package dev.devature.penguin_api.controller;

import dev.devature.penguin_api.entity.User;
import dev.devature.penguin_api.service.RegisterService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

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

    /**
     * Sending a mock http request to POST localhost:8080/api/v1/users/registration with valid credentials.
     *  Expected Response:
     *  Status Code: 201
     *  Response Body: Registration successful.
     *  /TODO: Fix the CSRF permission issue instead of using admin.
     */
    @Test
    public void registerUserSucceed() throws Exception {
        User user = new User("johnsmith@example.com", "Password_1");
        String userJson = objectMapper.writeValueAsString(user);

        when(registerService.checkEmailAvailable(user.getEmail())).thenReturn(true);

        when(registerService.registerUser(user)).thenReturn(user);

        this.mockMvc.perform(post("/api/v1/user/registration")
                        .with(csrf())
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().is(201))
                .andExpect(content()
                        .string(containsString("Registration successful.")))
                .andDo(document("registration/success"));
    }

    /**
     * Sending a mock http request to POST localhost:8080/api/v1/users/registration with valid credentials.
     *  Expected Response:
     *  Status Code: 400
     *  Response Body: Registration unsuccessful. Failed to create an account.
     *  /TODO: Fix the CSRF permission issue instead of using admin.
     */
    @Test
    public void registerUserFailed() throws Exception {
        User user = new User("johnsmith@example.com", "Password_1");
        String userJson = objectMapper.writeValueAsString(user);

        when(registerService.checkEmailAvailable(user.getEmail())).thenReturn(true);

        when(registerService.registerUser(user)).thenReturn(null);

        this.mockMvc.perform(post("/api/v1/user/registration")
                .with(csrf())
                .contentType("application/json")
                .content(userJson))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .string(containsString("Registration unsuccessful. Failed to create an account.")))
                .andDo(document("registration/failure"));
    }

    /**
     * Sending a mock http request to POST localhost:8080/api/v1/users/registration with valid credentials
     *  Expected Response:
     *  Status Code: 400
     *  Response Body: Registration unsuccessful. Failed to create an account.
     *  /TODO: Fix the CSRF permission issue instead of using admin.
     */
    @Test
    public void registerUser_WithEmailConflict() throws Exception {
        User user = new User("testsmith@example.com", "Password_1");
        String userJson = objectMapper.writeValueAsString(user);

        // TODO: Use H2 for this test since we need a stateful testing. Mock is stateless.

        when(registerService.checkEmailAvailable(user.getEmail())).thenReturn(false);

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
