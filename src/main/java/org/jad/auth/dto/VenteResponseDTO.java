package org.jad.auth.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenteResponseDTO {
    private Long id;
    private String nomProduit;
    private int quantiteVendue;
    private double prixVenteUnitaire;
    private LocalDate dateVente;
}
