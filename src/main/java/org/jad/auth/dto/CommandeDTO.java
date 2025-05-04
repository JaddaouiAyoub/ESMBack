package org.jad.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jad.auth.entity.Commande;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class CommandeDTO {
    private Long id;
    private String codeCommande;
    private String dateCreation;
    private String statut;
    private double montantTotal;
    private FournisseurDTOFC fournisseur;
    private List<LigneCommandeDTO> lignes;

    public CommandeDTO(Commande c) {
        this.id = c.getId();
        this.codeCommande = c.getCodeCommande();
        this.dateCreation = c.getDateCreation().toString();
        this.statut = c.getStatut().name();
        this.montantTotal = c.getMontantTotal();
        this.fournisseur = new FournisseurDTOFC(c.getFournisseur());
        this.lignes = c.getLignesCommande()
                .stream()
                .map(LigneCommandeDTO::new)
                .collect(Collectors.toList());
    }
}
