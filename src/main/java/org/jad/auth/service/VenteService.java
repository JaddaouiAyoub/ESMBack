package org.jad.auth.service;

import org.jad.auth.dto.VenteRequestDTO;
import org.jad.auth.dto.VenteResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface VenteService {
    VenteResponseDTO enregistrerVente(VenteRequestDTO dto);
    Page<VenteResponseDTO> rechercherVentes(Optional<String> nomProduit, Optional<LocalDate> dateDebut, Optional<LocalDate> dateFin, Pageable pageable);
    // VenteService.java (dans l’interface ou la classe implémentée)
    VenteResponseDTO getVenteById(Long id);
    void supprimerVente(Long id);

    List<Map<String, Object>> getTopProduitsVendus();
}
