package org.jad.auth.dto;

import lombok.Data;

@Data
public class FournisseurDTO {
    private Long id;
    private String username;
    private String email;
//    private String firstName;
//    private String lastName;
    private String raisonSociale;
    private String phoneNumber;
    private String code; // Code généré comme F01, F02
}
