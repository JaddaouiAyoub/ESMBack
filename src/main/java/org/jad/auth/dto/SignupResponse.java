package org.jad.auth.dto;

import lombok.Data;
import org.jad.auth.enums.Role;
@Data

public class SignupResponse {
    private String username;
    private String email;
    private String password;

    private String firstName;
    private Role role;
    private String lastName;

    private String phoneNumber;
}
