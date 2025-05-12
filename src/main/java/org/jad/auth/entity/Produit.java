package org.jad.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private int quantiteStock;
    private int quantiteVendu;
    private int reorderPoint; // seuil de réapprovisionnement
    private int stockInitiale; // nouveau champ
    private int leadTime; // temps d'arrivée en jours
    private double prix;
    @Column(nullable = false)
    private int stockSecurite = 10; // Valeur par défaut

    @ManyToOne
    @JoinColumn(name = "fournisseur_id")
    private Fournisseur fournisseur;
}
