package org.jad.auth.repository;

import org.jad.auth.entity.FournisseurCodeCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FournisseurCodeCounterRepository extends JpaRepository<FournisseurCodeCounter, Long> {
    // Méthode personnalisée si nécessaire
    // Par exemple, récupérer le compteur spécifique
    Optional<FournisseurCodeCounter> findById(Long id);
}
