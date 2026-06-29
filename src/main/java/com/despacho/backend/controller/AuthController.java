package com.despacho.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.despacho.backend.dto.LoginRequest;
import com.despacho.backend.dto.LoginResponse;
import com.despacho.backend.dto.RegisterRequest;
import com.despacho.backend.services.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
        @RequestBody LoginRequest request) {
            return ResponseEntity.ok(authService.login(request));
        }

    @PostMapping("/register")
    public  ResponseEntity<String> register(
        @RequestBody RegisterRequest request) {
            return ResponseEntity.ok(authService.register(request));
        }
}
