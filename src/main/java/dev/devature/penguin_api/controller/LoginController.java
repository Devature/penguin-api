package dev.devature.penguin_api.controller;

import dev.devature.penguin_api.entity.AppUser;
import dev.devature.penguin_api.model.JwtToken;
import dev.devature.penguin_api.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/user")
public class LoginController {

    private final LoginService loginService;

    @Autowired
    LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * @param appUser a {@code User} mapped from JSON data
     * @return a 200 response if successful or 401 response if not
     */
    @PostMapping("/login")
    public ResponseEntity<JwtToken> login(@RequestBody AppUser appUser) {
        JwtToken jwtToken = loginService.authenticate(appUser);
        return jwtToken != null ? ResponseEntity.ok(jwtToken) :
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}


