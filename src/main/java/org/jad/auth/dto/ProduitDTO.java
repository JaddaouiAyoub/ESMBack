package org.jad.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProduitDTO {
    private Long id;
    private String nom;
    private int quantiteStock;
    private int quantiteVendu;
    private int reorderPoint;
    private double prix;
    private Long fournisseurId; // seulement l'ID du fournisseur, pas l'objet complet
}
