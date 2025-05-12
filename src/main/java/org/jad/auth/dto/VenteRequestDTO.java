package org.jad.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenteRequestDTO {
    private Long produitId;
    private int quantite;
    private double prixVenteUnitaire;
}
