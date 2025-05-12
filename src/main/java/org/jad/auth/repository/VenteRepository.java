package org.jad.auth.repository;

import org.jad.auth.dto.TopProduitVenduProjection;
import org.jad.auth.entity.Vente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface VenteRepository extends JpaRepository<Vente, Long>, JpaSpecificationExecutor<Vente> {
    List<Vente> findByProduitId(Long produitId);

    List<Vente> findByProduitIdAndDateVenteBetween(Long produitId, LocalDate debut, LocalDate fin);
    // VenteRepository.java
    @Query("SELECT v.produit.nom AS produitNom, SUM(v.quantiteVendue) AS totalVendu " +
            "FROM Vente v GROUP BY v.produit.nom ORDER BY totalVendu DESC LIMIT 5")
    List<TopProduitVenduProjection> findTopProduitsVendus();
}
