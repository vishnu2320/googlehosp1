package com.google.hospital.admin.controller;

import com.google.hospital.admin.dto.AuthResponse;
import com.google.hospital.admin.dto.LoginRequest;
import com.google.hospital.admin.entity.AppUser;
import com.google.hospital.admin.service.AuthService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AppUser> register(@RequestBody AppUser user) {
        AppUser created = authService.register(user);
        return ResponseEntity.ok(created);
    }
}
