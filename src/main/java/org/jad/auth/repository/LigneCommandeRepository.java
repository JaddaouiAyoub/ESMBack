package org.jad.auth.repository;

import org.jad.auth.entity.LigneCommande;
import org.jad.auth.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LigneCommandeRepository extends JpaRepository<LigneCommande, Long> {
    List<LigneCommande> findByCommandeId(Long commandeId);

    List<LigneCommande> findByProduit(Produit produit);
}
