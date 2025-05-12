package org.jad.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardDTO {
    private long totalCommandes;
    private long commandesEnCours;
    private long commandesPartiellementRecues;
    private long commandesAnnulees;
    private long commandesEnAttente;
    private long commandesRecues;
    private long commandesEnRetard;
    private long commandesConfirmees;
    private double montantTotalCommandes;
    private long produitsSousSeuil;
    private double montantTotalVentes; // <-- AJOUT ICI
    private Map<String, Long> commandesParStatut; // Ex: {"EN_COURS": 4, "RECUE": 10}
    private Map<String, Long> commandesParFournisseur; // {"F01": 5, "F02": 3}
    private Map<String, Long> topProduitsCommandes; // {"ProduitA": 120, "ProduitB": 80}
}
