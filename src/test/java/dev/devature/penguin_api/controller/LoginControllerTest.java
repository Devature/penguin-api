package dev.devature.penguin_api.controller;

import dev.devature.penguin_api.entity.User;
import dev.devature.penguin_api.service.LoginService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.mock.mockito.MockBean;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class LoginControllerTest extends RequestsTest {
    @MockBean
    private LoginService loginService;

    @Test
    public void loginSucceed() throws Exception {
        User user = new User("agoodtestemail@testemail.com","Th1sisvalidpass!");
        String userJson = objectMapper.writeValueAsString(user);

        when(loginService.checkValidity(user)).thenReturn(true);
        when(loginService.authenticate(user)).thenReturn(true);

        this.mockMvc.perform(post("/api/v1/user/login")
                        .with(csrf())
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().is(200))
                .andExpect(content()
                        .string(containsString("Login successful")))
                .andDo(document("login/success"));
    }

    @Test
    public void loginFailure() throws Exception {
        User user = new User("testemail2testemail.com", "invalid");
        String userJson = objectMapper.writeValueAsString(user);

        when(loginService.checkValidity(user)).thenReturn(true);
        when(loginService.authenticate(user)).thenReturn(false);

        this.mockMvc.perform(post("/api/v1/user/login")
                        .with(csrf())
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().is(401))
                .andExpect(content()
                        .string(containsString("Invalid credentials")))
                .andDo(document("login/failure"));
    }
}
