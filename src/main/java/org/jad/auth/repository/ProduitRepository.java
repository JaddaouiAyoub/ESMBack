package org.jad.auth.repository;

import org.jad.auth.dto.ProduitNomIdDTO;
import org.jad.auth.entity.Produit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProduitRepository extends JpaRepository<Produit, Long>, JpaSpecificationExecutor<Produit> {
    List<Produit> findByFournisseurId(Long fournisseurId);
    @Query("SELECT new org.jad.auth.dto.ProduitNomIdDTO(p.id, p.nom) FROM Produit p")
    List<ProduitNomIdDTO> findAllNomId();

    Page<Produit> findAll(Specification<Produit> spec, Pageable pageable);
}
