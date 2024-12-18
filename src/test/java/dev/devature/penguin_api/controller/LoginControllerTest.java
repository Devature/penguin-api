package dev.devature.penguin_api.controller;

import dev.devature.penguin_api.entity.AppUser;
import dev.devature.penguin_api.model.JwtToken;
import dev.devature.penguin_api.service.LoginService;
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

class LoginControllerTest extends RequestsTest {
    @MockBean
    private LoginService loginService;

    @Test
    public void loginSucceed() throws Exception {
        AppUser appUser = new AppUser("agoodtestemail@testemail.com","Th1sisvalidpass!");
        String userJson = new JSONObject()
                .put("email", appUser.getEmail())
                .put("password", appUser.getPassword()).toString();

        JwtToken jwtToken = new JwtToken("abc123");
        String jwtTokenJson = objectMapper.writeValueAsString(jwtToken);

        when(loginService.authenticate(appUser)).thenReturn(jwtToken);

        this.mockMvc.perform(post("/api/v1/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().is(200))
                .andExpect(content()
                        .string(containsString(jwtTokenJson)))
                .andDo(document("login/success"));
    }

    @Test
    public void loginFailure() throws Exception {
        AppUser appUser = new AppUser("testemail2testemail.com", "invalid");
        String userJson = new JSONObject()
                .put("email", appUser.getEmail())
                .put("password", appUser.getPassword()).toString();

        when(loginService.authenticate(appUser)).thenReturn(null);

        this.mockMvc.perform(post("/api/v1/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().is(401))
                .andDo(document("login/failure"));
    }
}
