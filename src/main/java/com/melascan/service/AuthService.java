package com.melascan.service;

import com.melascan.entity.User;
import com.melascan.exception.ApiBusinessException;
import com.melascan.model.auth.request.LoginRequest;
import com.melascan.model.auth.request.SignUpRequest;
import com.melascan.model.auth.response.LoginResponse;
import com.melascan.model.auth.response.SignUpResponse;
import com.melascan.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;

    public SignUpResponse handleSignUp(SignUpRequest requestDTO) {
        User user = userService.createUserFromSignupRequest(requestDTO);
        return SignUpResponse.fromModel(user);
    }

    public LoginResponse handleLogin(LoginRequest requestDTO) {
        User user = userService.findUserByEmail(requestDTO.getEmail());
        if (!userService.checkPassword(requestDTO.getPassword(), user.getPassword())) {
            throw new ApiBusinessException("Email or password is incorrect.");
        }
        String token = JwtUtil.generateToken(user);
        return LoginResponse.builder().token(token).userId(user.getId()).build();
    }

    public boolean validateTokenDue(String bearer) {
        return !JwtUtil.isTokenExpired(bearer);
    }
}
