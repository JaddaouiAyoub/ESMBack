package org.jad.auth.repository;

import org.jad.auth.entity.SousLigneCommande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SousLigneCommandeRepository extends JpaRepository<SousLigneCommande, Long> {
    List<SousLigneCommande> findByLigneCommandeId(Long ligneCommandeId);
}
