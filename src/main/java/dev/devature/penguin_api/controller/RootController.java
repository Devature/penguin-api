package dev.devature.penguin_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.HashMap;

@RestController
public class RootController {

    @GetMapping("/api/test")
    public Map<String, Object> getRootData() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to Penguin API");
        response.put("status", "success");
        response.put("version", "1.0");

        return response;
    }
}
