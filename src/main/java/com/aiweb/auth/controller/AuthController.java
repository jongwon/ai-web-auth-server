package com.aiweb.auth.controller;

import com.aiweb.auth.dto.AuthResponse;
import com.aiweb.auth.dto.LoginRequest;
import com.aiweb.auth.dto.RegisterRequest;
import com.aiweb.auth.entity.User;
import com.aiweb.auth.security.CustomUserPrincipal;
import com.aiweb.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "인증 API", description = "회원가입, 로그인, 사용자 정보 조회")
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/register")
    @Operation(summary = "회원가입", description = "이메일과 비밀번호로 회원가입")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        AuthResponse response = authService.register(registerRequest);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/me")
    @Operation(summary = "현재 사용자 정보", description = "JWT 토큰으로 현재 로그인한 사용자 정보 조회")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal CustomUserPrincipal userPrincipal) {
        User user = authService.getCurrentUser(userPrincipal.getEmail());
        return ResponseEntity.ok(user);
    }
}