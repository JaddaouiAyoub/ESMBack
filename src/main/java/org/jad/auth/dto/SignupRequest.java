package org.jad.auth.dto;

import lombok.Data;
import org.jad.auth.enums.Role;

@Data
public class SignupRequest {
    private String username;
    private String email;
    private String password;

    private String firstName;
    private String role;
    private String lastName;

    private String phoneNumber;
}
