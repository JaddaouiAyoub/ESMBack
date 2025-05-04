package org.jad.auth.repository;

import org.jad.auth.entity.HistoriqueCommande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoriqueCommandeRepository extends JpaRepository<HistoriqueCommande, Long> {
    List<HistoriqueCommande> findByCommandeId(Long commandeId);
}
