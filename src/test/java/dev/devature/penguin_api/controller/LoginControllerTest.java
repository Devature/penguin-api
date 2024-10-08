package dev.devature.penguin_api.controller;

import dev.devature.penguin_api.entity.User;
import dev.devature.penguin_api.model.JwtToken;
import dev.devature.penguin_api.service.LoginService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoginControllerTest extends RequestsTest {
    @MockBean
    private LoginService loginService;

    @Test
    public void loginSucceed() throws Exception {
        User user = new User("agoodtestemail@testemail.com","Th1sisvalidpass!");
        String userJson = new JSONObject()
                .put("email", user.getEmail())
                .put("password", user.getPassword()).toString();

        JwtToken jwtToken = new JwtToken("abc123");
        String jwtTokenJson = objectMapper.writeValueAsString(jwtToken);

        when(loginService.authenticate(user)).thenReturn(jwtToken);

        this.mockMvc.perform(post("/api/v1/user/login")
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().is(200))
                .andExpect(content()
                        .string(containsString(jwtTokenJson)))
                .andDo(document("login/success"));
    }

    @Test
    public void loginFailure() throws Exception {
        User user = new User("testemail2testemail.com", "invalid");
        String userJson = new JSONObject()
                .put("email", user.getEmail())
                .put("password", user.getPassword()).toString();

        when(loginService.authenticate(user)).thenReturn(null);

        this.mockMvc.perform(post("/api/v1/user/login")
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().is(401))
                .andDo(document("login/failure"));
    }
}
