package org.jad.auth.repository;

import org.jad.auth.entity.Commande;
import org.jad.auth.enums.StatutCommande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommandeRepository extends JpaRepository<Commande, Long> {
    Optional<Commande> findByCodeCommande(String codeCommande);

    List<Commande> findByStatut(StatutCommande statutCommande);

    boolean existsByCodeCommande(String code);
}
