package org.jad.auth.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LigneCommandeModificationDTO {
    private Long id;
    private int quantiteCommandee;
    private double prixUnitaire;
    private LocalDate dateLivraisonPrevue;
    private LocalDate dateDexpeditionConfirmee;
}
