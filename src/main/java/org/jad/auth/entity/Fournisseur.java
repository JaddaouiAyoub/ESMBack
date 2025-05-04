package org.jad.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Fournisseur extends User {
    @Column(unique = true, nullable = false)
    private String code;  // Ex: F01, F02
    private String raisonSociale;
    @OneToMany(mappedBy = "fournisseur")
    private List<Produit> produits;  // Fournisseur peut avoir plusieurs produits
    @OneToMany(mappedBy = "fournisseur")
    private List<Commande> commandes;
}
