package org.jad.auth.repository;

import aj.org.objectweb.asm.commons.Remapper;
import org.jad.auth.dto.FournisseurNomIdDTO;
import org.jad.auth.entity.Fournisseur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {
    Optional<Fournisseur> findByCode(String code);
    @Query("SELECT new org.jad.auth.dto.FournisseurNomIdDTO(f.id, f.raisonSociale) FROM Fournisseur f")

    List<FournisseurNomIdDTO> findAllNomId();

    Page<Fournisseur> findByRaisonSocialeContainingIgnoreCaseAndUsernameContainingIgnoreCase(
            String raisonSociale,
            String username,
            Pageable pageable
    );}
