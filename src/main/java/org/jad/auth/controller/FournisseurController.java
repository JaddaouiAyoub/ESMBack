package org.jad.auth.controller;

import lombok.RequiredArgsConstructor;
import org.jad.auth.dto.FournisseurDTO;
import org.jad.auth.service.FournisseurService;
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
    @GetMapping
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
}
