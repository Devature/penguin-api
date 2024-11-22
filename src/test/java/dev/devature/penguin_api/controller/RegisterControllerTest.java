package dev.devature.penguin_api.controller;

import dev.devature.penguin_api.entity.Users;
import dev.devature.penguin_api.enums.RegisterResult;
import dev.devature.penguin_api.service.RegisterService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RegisterControllerTest extends RequestsTest {
    @MockBean
    private RegisterService registerService;

    @Test
    public void register_WithUserSucceed() throws Exception {
        Users users = new Users("johnsmith@example.com", "Password_1");
        String userJson = new JSONObject()
                .put("email", users.getEmail())
                .put("password", users.getPassword()).toString();

        when(registerService.checkEmailAvailable(users.getEmail())).thenReturn(true);

        when(registerService.registerUser(users)).thenReturn(RegisterResult.SUCCESS);

        this.mockMvc.perform(post("/api/v1/user/registration")
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().is(201))
                .andExpect(content()
                        .string(containsString("Registration successful.")))
                .andDo(document("registration/success"));
    }

    @Test
    public void register_WithUserTotalFailed() throws Exception {
        Users users = new Users("johnsmith@example.com", "Password_1");
        String userJson = new JSONObject()
                .put("email", users.getEmail())
                .put("password", users.getPassword()).toString();

        when(registerService.checkEmailAvailable(users.getEmail())).thenReturn(true);

        when(registerService.registerUser(users)).thenReturn(RegisterResult.UNKNOWN_ERROR);

        this.mockMvc.perform(post("/api/v1/user/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().is(422))
                .andExpect(content()
                        .string(containsString("Invalid registration data. " +
                                "Please review your input and try again.")))
                .andDo(document("registration/failure"));
    }

    @Test
    public void register_WithBadData() throws Exception {
        Users users = new Users("johnsmith@example.com", "Password1");
        String userJson = new JSONObject()
                .put("email", users.getEmail())
                .put("password", users.getPassword()).toString();

        when(registerService.checkEmailAvailable(users.getEmail())).thenReturn(true);

        when(registerService.registerUser(users)).thenReturn(RegisterResult.INVALID_ACCOUNT_INFO);

        this.mockMvc.perform(post("/api/v1/user/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .string(containsString("Account information is invalid.")))
                .andDo(document("registration/failure"));
    }

    @Test
    public void registerUser_WithEmailConflict() throws Exception {
        Users users = new Users("testsmith@example.com", "Password_1");
        String userJson = new JSONObject()
                .put("email", users.getEmail())
                .put("password", users.getPassword()).toString();

        when(registerService.registerUser(users)).thenReturn(RegisterResult.EMAIL_TAKEN);

        this.mockMvc.perform(post("/api/v1/user/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isConflict())
                .andExpect(content()
                        .string(containsString("Someone is already using that email")))
                .andDo(document("registration/failure"));
    }
}
