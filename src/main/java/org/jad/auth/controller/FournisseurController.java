package org.jad.auth.controller;

import lombok.RequiredArgsConstructor;
import org.jad.auth.dto.FournisseurDTO;
import org.jad.auth.dto.FournisseurNomIdDTO;
import org.jad.auth.service.FournisseurService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("*")
@RestController
@RequestMapping("/api/fournisseurs")
@RequiredArgsConstructor
public class FournisseurController {

    private final FournisseurService fournisseurService;

    // ➤ Créer un fournisseur
    @PostMapping
    public ResponseEntity<FournisseurDTO> createFournisseur(@RequestBody FournisseurDTO fournisseurDTO) {
        System.out.println(fournisseurDTO);
        FournisseurDTO created = fournisseurService.createFournisseur(fournisseurDTO);
        return ResponseEntity.ok(created);
    }

    // ➤ Obtenir tous les fournisseurs
    @GetMapping("/page")
    public ResponseEntity<Page<FournisseurDTO>> getFournisseursAvecPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(fournisseurService.getFournisseursAvecPagination(page, size));
    }
    @GetMapping
    public ResponseEntity<Page<FournisseurDTO>> getFournisseursFiltres(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String raisonSociale,
            @RequestParam(required = false) String username
    ) {
        return ResponseEntity.ok(
                fournisseurService.getFournisseursFiltres(raisonSociale, username, page, size)
        );
    }

    // ➤ Obtenir tous les fournisseurs
    @GetMapping("/all")
    public ResponseEntity<List<FournisseurDTO>> getAllFournisseurs() {
        List<FournisseurDTO> fournisseurs = fournisseurService.getAllFournisseurs();
        return ResponseEntity.ok(fournisseurs);
    }


    // ➤ Obtenir un fournisseur par ID
    @GetMapping("/{id}")
    public ResponseEntity<FournisseurDTO> getFournisseur(@PathVariable Long id) {
        FournisseurDTO fournisseur = fournisseurService.getFournisseur(id);
        return ResponseEntity.ok(fournisseur);
    }

    // ➤ Supprimer un fournisseur
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFournisseur(@PathVariable Long id) {
        fournisseurService.deleteFournisseur(id);
        return ResponseEntity.noContent().build();
    }
    // Méthode pour modifier un fournisseur
    @PutMapping("/{id}")
    public ResponseEntity<FournisseurDTO> updateFournisseur(@PathVariable Long id, @RequestBody FournisseurDTO fournisseurDTO) {
        FournisseurDTO updatedFournisseur = fournisseurService.updateFournisseur(id, fournisseurDTO);
        return ResponseEntity.ok(updatedFournisseur);
    }
    @GetMapping("/nom-id")
    public ResponseEntity<List<FournisseurNomIdDTO>> getFournisseursNomId() {
        List<FournisseurNomIdDTO> fournisseurs = fournisseurService.getAllFournisseurNomIdDTO();
        return ResponseEntity.ok(fournisseurs);
    }
}
