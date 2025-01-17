package com.melascan.controller;

import com.melascan.model.auth.request.LoginRequest;
import com.melascan.model.auth.request.SignUpRequest;
import com.melascan.model.auth.response.LoginResponse;
import com.melascan.model.auth.response.SignUpResponse;
import com.melascan.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public SignUpResponse signup(@Valid @RequestBody SignUpRequest requestDTO) {
        return authService.handleSignUp(requestDTO);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest requestDTO) {
        return authService.handleLogin(requestDTO);
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        boolean isValid = authService.validateTokenDue(token.replace("Bearer ", ""));
        if (isValid) {
            return ResponseEntity.ok().body("Token is valid");
        } else {
            return ResponseEntity.status(401).body("Token is expired or invalid");
        }
    }


}
