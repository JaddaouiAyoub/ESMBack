package org.jad.auth.dto;

import lombok.Data;
import org.jad.auth.entity.Fournisseur;

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
    private FournisseurDTO mapToDTO(Fournisseur fournisseur) {
        FournisseurDTO dto = new FournisseurDTO();
        dto.setId(fournisseur.getId());
        dto.setUsername(fournisseur.getUsername());
        dto.setEmail(fournisseur.getEmail());
        dto.setRaisonSociale(fournisseur.getRaisonSociale());
        dto.setPhoneNumber(fournisseur.getPhoneNumber());
        dto.setCode(fournisseur.getCode());
        return dto;
    }

}
