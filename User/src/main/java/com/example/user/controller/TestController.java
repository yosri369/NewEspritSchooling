package com.example.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/protected")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<String> protectedEndpoint() {
        return ResponseEntity.ok("You are authenticated and authorized!");
    }
}
