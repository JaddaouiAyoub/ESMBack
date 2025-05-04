package org.jad.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.jad.auth.enums.StatutCommande;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String codeCommande; // Ex: 4501, 4502...

    private LocalDate dateCreation;

    @Column(length = 50)
    @Enumerated(EnumType.STRING)
    private StatutCommande statut;

    private double montantTotal;

    private int quantity;

    @ManyToOne
    @JsonIgnoreProperties({"produits", "commandes"}) // empêche la boucle
    private Fournisseur fournisseur;

    @OneToMany(mappedBy = "commande")
    private List<LigneCommande> lignesCommande;

    @OneToMany(mappedBy = "commande")
    @JsonIgnoreProperties("commande")
    private List<HistoriqueCommande> historiques;
//    @ManyToOne
//    @JoinColumn(name = "client_id")
//    private Client client;
}
