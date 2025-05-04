package org.jad.auth.dto;

import lombok.Data;
import org.jad.auth.entity.LigneCommande;

import java.time.LocalDate;

@Data
public class LigneCommandeDTO {
    private Long id;
    private String nomProduit;
    private int quantiteCommandee;
    private double prixUnitaire;
    private LocalDate dateLivraisonPrevue;
    private LocalDate dateDexpeditionConfirmee;

    public LigneCommandeDTO(LigneCommande ligne) {
        this.id = ligne.getId();
        this.nomProduit = ligne.getProduit().getNom();
        this.quantiteCommandee = ligne.getQuantiteCommandee();
        this.prixUnitaire = ligne.getPrixUnitaire();
        this.dateLivraisonPrevue = ligne.getDateLivraisonPrevue();
        this.dateDexpeditionConfirmee = ligne.getDateDexpeditionConfirmee();
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
