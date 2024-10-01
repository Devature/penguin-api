package dev.devature.penguin_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.devature.penguin_api.entity.User;
import dev.devature.penguin_api.service.RegisterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


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
     *  /TODO: Fix the CSRF permission issue instead of using admin.
     */
    @Test
    @WithMockUser("ADMIN")
    public void registerUserSucceed() throws Exception {
        User user = new User("johnsmith@example.com", "Password_1");
        String userJson = objectMapper.writeValueAsString(user);

        when(registerService.registerUser(user)).thenReturn(user);

        this.mockMvc.perform(post("/api/v1/user/registration")
                        .with(csrf())
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
     *  /TODO: Fix the CSRF permission issue instead of using admin.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    public void registerUserFailed() throws Exception {
        User user = new User("johnsmith@example.com", "Password_1");
        String userJson = objectMapper.writeValueAsString(user);

        when(registerService.registerUser(user)).thenReturn(null);

        this.mockMvc.perform(post("/api/v1/user/registration")
                .with(csrf())
                .contentType("application/json")
                .content(userJson))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .string(containsString("Registration unsuccessful. Failed to create an account.")));
    }


    /**
     * Sending a mock http request to POST localhost:8080/api/v1/users/registration with valid credentials
     *
     *  Expected Response:
     *  Status Code: 409
     *  Response Body: Someone is already using that email.
     *  /TODO: Fix the CSRF permission issue instead of using admin.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    public void registerEmailAlreadyUsed() throws Exception {
        User user1 = new User("testsmith@example.com", "Password_1");
        String userJson1 = objectMapper.writeValueAsString(user1);

        // TODO: Use H2 for this test since we need a stateful testing. Mock is stateless.

        when(registerService.checkEmailAvailable(user1.getEmail())).thenReturn(false);

        this.mockMvc.perform(post("/api/v1/user/registration")
                        .with(csrf())
                        .contentType("application/json")
                        .content(userJson1))
                .andExpect(status().isConflict())
                .andExpect(content()
                        .string(containsString("Someone is already using that email")));
    }
}
