package org.jad.auth.controller;

import lombok.RequiredArgsConstructor;
import org.jad.auth.dto.ProduitDTO;
import org.jad.auth.service.ProduitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("*")
@RestController
@RequestMapping("/api/produits")
@RequiredArgsConstructor
public class ProduitController {

    private final ProduitService produitService;

    // ➤ Créer un produit
    @PostMapping
    public ResponseEntity<ProduitDTO> createProduit(@RequestBody ProduitDTO produitDTO) {
        ProduitDTO created = produitService.createProduit(produitDTO);
        return ResponseEntity.ok(created);
    }

    // ➤ Récupérer tous les produits
    @GetMapping
    public ResponseEntity<List<ProduitDTO>> getAllProduits() {
        return ResponseEntity.ok(produitService.getAllProduits());
    }

    // ➤ Récupérer un produit par ID
    @GetMapping("/{id}")
    public ResponseEntity<ProduitDTO> getProduit(@PathVariable Long id) {
        return ResponseEntity.ok(produitService.getProduit(id));
    }

    // ➤ Mettre à jour un produit
    @PutMapping("/{id}")
    public ResponseEntity<ProduitDTO> updateProduit(@PathVariable Long id, @RequestBody ProduitDTO produitDTO) {
        return ResponseEntity.ok(produitService.updateProduit(id, produitDTO));
    }

    // ➤ Supprimer un produit
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduit(@PathVariable Long id) {
        produitService.deleteProduit(id);
        return ResponseEntity.noContent().build();
    }

    // ➤ Récupérer les produits d'un fournisseur
    @GetMapping("/fournisseur/{fournisseurId}")
    public ResponseEntity<List<ProduitDTO>> getProduitsByFournisseur(@PathVariable Long fournisseurId) {
        return ResponseEntity.ok(produitService.getProduitsByFournisseur(fournisseurId));
    }

    // ➤ Affecter un produit à un fournisseur
    @PutMapping("/{produitId}/fournisseur/{fournisseurId}")
    public ResponseEntity<ProduitDTO> affecterProduitAFournisseur(
            @PathVariable Long produitId,
            @PathVariable Long fournisseurId) {
        return ResponseEntity.ok(produitService.affecterProduitAFournisseur(produitId, fournisseurId));
    }

    // ➤ Récupérer les produits sous le seuil ROP
    @GetMapping("/sous-seuil")
    public ResponseEntity<List<ProduitDTO>> getProduitsSousSeuil() {
        return ResponseEntity.ok(produitService.getProduitsSousSeuil());
    }
}
