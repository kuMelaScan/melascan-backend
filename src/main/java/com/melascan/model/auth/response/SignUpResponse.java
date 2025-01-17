package com.melascan.model.auth.response;

import com.melascan.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class SignUpResponse {

    private UUID id;

    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    private String email;

    private String password;

    public static SignUpResponse fromModel(User user) {
        return SignUpResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}
