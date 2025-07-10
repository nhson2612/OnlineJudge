package org.example.judge.auth.controller;

import org.example.judge.auth.domain.AuthResponse;
import org.example.judge.auth.domain.RegisterReq;
import org.example.judge.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    public final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(RegisterReq registerReq) {
        AuthResponse response = authService.register(registerReq.username(), registerReq.password());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(RegisterReq registerReq) {
        AuthResponse response = authService.login(registerReq.username(), registerReq.password());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(String token) {
        authService.logout(token);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(String token) {
        AuthResponse response = authService.refreshToken(token);
        return ResponseEntity.ok(response);
    }
}
