package org.jad.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateVente;

    private int quantiteVendue;
    private double prixVenteUnitaire;

    @ManyToOne
    @JoinColumn(name = "produit_id")
    private Produit produit;
    
    // Optionnel : client, utilisateur, point de vente, etc.
}
