package com.melascan.service;

import com.melascan.entity.User;
import com.melascan.exception.ApiBusinessException;
import com.melascan.mapper.UserMapper;
import com.melascan.model.auth.request.SignUpRequest;
import com.melascan.repository.UserPhotoRepository;
import com.melascan.repository.UserRepository;
import com.melascan.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserPhotoRepository userPhotoRepository;

    public User retrieveUserById(UUID userId) {
        log.info("UserService -> retrieveUserById started: userId={}", userId.toString());
        if (StringUtils.isEmpty(userId.toString())) throw new ApiBusinessException("userId is a required field.");
        User user =  userRepository.findById(userId).orElseThrow(() -> new ApiBusinessException("User not found with id: " + userId));
        log.info("UserService -> retrieveUserById completed!");
        return user;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ApiBusinessException("User not found with email: " + email));
    }

    public User createUserFromSignupRequest(SignUpRequest requestDTO) {

        log.info("UserService -> createUserFromSignupRequest started: requestDTO={}", requestDTO.toString());
        User user = new User();
        UserMapper.signUpDtoToUser(user, requestDTO);
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        User savedEntity = userRepository.save(user);
        log.info("UserService -> createUserFromSignupRequest completed!");
        return savedEntity;

    }

    public Boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }



}
