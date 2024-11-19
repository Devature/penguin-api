package dev.devature.penguin_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

// todo: remove this controller eventually

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("The time is %s.".formatted(DateTimeFormatter.ofPattern("hh:mm:ss a z").format(ZonedDateTime.now())));
    }
}
