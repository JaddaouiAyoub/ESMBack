package org.jad.auth.controller;

import lombok.RequiredArgsConstructor;
import org.jad.auth.dto.VenteRequestDTO;
import org.jad.auth.dto.VenteResponseDTO;
import org.jad.auth.service.VenteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/ventes")
@RequiredArgsConstructor
public class VenteController {

    private final VenteService venteService;

    // 👉 Enregistrer une vente
    @PostMapping
    public VenteResponseDTO enregistrerVente(@RequestBody VenteRequestDTO dto) {
        return venteService.enregistrerVente(dto);
    }

    // 👉 Rechercher avec pagination + filtres dynamiques
    @GetMapping
    public Page<VenteResponseDTO> rechercherVentes(
            @RequestParam Optional<String> nomProduit,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> dateFin,
            Pageable pageable
    ) {
        return venteService.rechercherVentes(nomProduit, dateDebut, dateFin, pageable);
    }

//     👉 Détails d’une vente
    @GetMapping("/{id}")
    public VenteResponseDTO getVente(@PathVariable Long id) {
        return venteService.getVenteById(id);
    }

    // 👉 Supprimer une vente
    @DeleteMapping("/{id}")
    public void supprimerVente(@PathVariable Long id) {
        venteService.supprimerVente(id);
    }
    @GetMapping("/top-produits-vendus")
    public ResponseEntity<List<Map<String, Object>>> getTopProduitsVendus() {
        return ResponseEntity.ok(venteService.getTopProduitsVendus());
    }
}
