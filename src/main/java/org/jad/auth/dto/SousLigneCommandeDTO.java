package org.jad.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jad.auth.entity.SousLigneCommande;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class SousLigneCommandeDTO {
    private Long id;
    private int quantiteRecue;
    private LocalDate dateReception;
    private Long ligneCommandeId; // pour lier à la ligne de commande d'origine

    public SousLigneCommandeDTO(SousLigneCommande sousLigne) {
        this.id = sousLigne.getId();
        this.quantiteRecue = sousLigne.getQuantiteRecue();
        this.dateReception = sousLigne.getDateReception();
        this.ligneCommandeId = sousLigne.getLigneCommande() != null ? sousLigne.getLigneCommande().getId() : null;
    }
}
