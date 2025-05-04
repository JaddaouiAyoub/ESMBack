package org.jad.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jad.auth.entity.Fournisseur;

@Data
@NoArgsConstructor
public class FournisseurDTOFC {
    private Long id;
    private String code;
    private String raisonSociale;
    public FournisseurDTOFC(Fournisseur f) {
        this.id = f.getId();
        this.code = f.getCode();
        this.raisonSociale = f.getRaisonSociale();
    }
}
