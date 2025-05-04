package org.jad.auth.repository;

import org.jad.auth.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
    List<Produit> findByFournisseurId(Long fournisseurId);
}
