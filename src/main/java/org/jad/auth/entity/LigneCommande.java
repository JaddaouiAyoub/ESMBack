package org.jad.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LigneCommande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantiteCommandee;
    private double prixUnitaire;
    private LocalDate dateLivraisonPrevue;
    private LocalDate dateDexpedition;
    private LocalDate dateDexpeditionConfirmee;
    private LocalDate dateReception;

    @ManyToOne
    private Produit produit;

    @ManyToOne
    private Commande commande;

    @OneToMany(mappedBy = "ligneCommande", cascade = CascadeType.ALL)
    private List<SousLigneCommande> sousLignes;
}
