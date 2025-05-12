package org.jad.auth.mapper;

import org.jad.auth.dto.ProduitDTO;
import org.jad.auth.entity.Fournisseur;
import org.jad.auth.entity.Produit;

public class ProduitMapper {

    public static ProduitDTO toDTO(Produit produit) {
        if (produit == null) {
            return null;
        }
        return ProduitDTO.builder()
                .id(produit.getId())
                .nom(produit.getNom())
                .quantiteStock(produit.getQuantiteStock())
                .quantiteVendu(produit.getQuantiteVendu())
                .reorderPoint(produit.getReorderPoint())
                .stockInitiale(produit.getStockInitiale())
                .leadTime(produit.getLeadTime())
                .prix(produit.getPrix())
                .fournisseurId(produit.getFournisseur() != null ? produit.getFournisseur().getId() : null)
                .build();
    }

    public static Produit toEntity(ProduitDTO dto, Fournisseur fournisseur) {
        if (dto == null) {
            return null;
        }
        return Produit.builder()
                .id(dto.getId())
                .nom(dto.getNom())
                .quantiteStock(dto.getQuantiteStock())
                .quantiteVendu(dto.getQuantiteVendu())
                .reorderPoint(dto.getReorderPoint())
                .stockInitiale(dto.getStockInitiale())
                .leadTime(dto.getLeadTime())
                .prix(dto.getPrix())
                .fournisseur(fournisseur)
                .build();
    }
}
