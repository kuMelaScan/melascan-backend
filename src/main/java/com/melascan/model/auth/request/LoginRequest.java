package com.melascan.model.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginRequest {

    @NotEmpty
    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty
    private String password;

}
