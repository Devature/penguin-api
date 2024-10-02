package dev.devature.penguin_api.interceptor;

import dev.devature.penguin_api.model.JwtToken;
import dev.devature.penguin_api.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashSet;
import java.util.Set;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    private static final Set<String> OPEN_ENDPOINTS;

    static {
        OPEN_ENDPOINTS = new HashSet<>();
        OPEN_ENDPOINTS.add("/api/v1/user/registration");
        OPEN_ENDPOINTS.add("/api/v1/user/login");
    }

    private final JwtService jwtService;

    @Autowired
    public AuthenticationInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")) return true;
        if (OPEN_ENDPOINTS.contains(request.getRequestURI())) return true;

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.sendError(401, "Invalid authorization header");
            return false;
        }

        JwtToken token = new JwtToken(authorizationHeader.substring("Bearer ".length()));
        if (this.jwtService.verifyToken(token).isPresent()) return true;
        else response.sendError(401, "Invalid authorization token");

        return false;
    }
}
