package org.jad.auth.dto;

import lombok.Data;
import org.jad.auth.entity.LigneCommande;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class LigneCommandeDTO {
    private Long id;
    private String nomProduit;
    private int quantiteCommandee;
    private double prixUnitaire;
    private LocalDate dateLivraisonPrevue;
    private LocalDate dateDexpeditionConfirmee;
    private List<SousLigneCommandeDTO> sousLignes; // ✅ ajout

    public LigneCommandeDTO(LigneCommande ligne) {
        this.id = ligne.getId();
        this.nomProduit = ligne.getProduit().getNom();
        this.quantiteCommandee = ligne.getQuantiteCommandee();
        this.prixUnitaire = ligne.getPrixUnitaire();
        this.dateLivraisonPrevue = ligne.getDateLivraisonPrevue();
        this.dateDexpeditionConfirmee = ligne.getDateDexpeditionConfirmee();
        this.sousLignes = ligne.getSousLignes() != null ?
                ligne.getSousLignes().stream()
                        .map(SousLigneCommandeDTO::new)
                        .collect(Collectors.toList())
                : null;
    }

    public LigneCommandeDTO( String nomProduit, int quantiteCommandee,double prixUnitaire, LocalDate dateLivraisonPrevue) {

        this.nomProduit = nomProduit;
        this.quantiteCommandee = quantiteCommandee;
        this.prixUnitaire = prixUnitaire;
        this.dateLivraisonPrevue = dateLivraisonPrevue;
    }

    public LigneCommandeDTO(String nomProduit, int quantiteCommandee, double prixUnitaire, LocalDate dateLivraisonPrevue, LocalDate dateDexpeditionConfirmee) {
        this.nomProduit = nomProduit;
        this.quantiteCommandee = quantiteCommandee;
        this.prixUnitaire = prixUnitaire;
        this.dateLivraisonPrevue = dateLivraisonPrevue;
        this.dateDexpeditionConfirmee = dateDexpeditionConfirmee;
    }
    // Getters / Setters
}
