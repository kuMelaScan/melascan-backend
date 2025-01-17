package com.melascan.mapper;


import com.melascan.entity.User;
import com.melascan.model.auth.request.SignUpRequest;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserMapper {

    private UserMapper() {
    }

    public static void signUpDtoToUser(User user, SignUpRequest signUpRequest) {

        if (Objects.nonNull(user) && Objects.nonNull(signUpRequest)) {
            user.setEmail(signUpRequest.getEmail());
            user.setPassword(signUpRequest.getPassword());
            user.setFirstName(signUpRequest.getFirstName());
            user.setLastName(signUpRequest.getLastName());
            user.setDateOfBirth(signUpRequest.getDateOfBirth());
        }

    }

}
