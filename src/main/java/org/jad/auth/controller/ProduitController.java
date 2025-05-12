package org.jad.auth.controller;

import lombok.RequiredArgsConstructor;
import org.jad.auth.dto.ProduitDTO;
import org.jad.auth.dto.ProduitNomIdDTO;
import org.jad.auth.service.ProduitService;
import org.springframework.data.domain.Page;
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
    @GetMapping("/all")
    public ResponseEntity<Page<ProduitDTO>> getProduitsAvecPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(produitService.getProduitsAvecPagination(page, size));
    }
    @GetMapping
    public ResponseEntity<Page<ProduitDTO>> getProduitsFiltres(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String nom,
            @RequestParam(required = false, defaultValue = "") String sousSeuil,
            @RequestParam(required = false) Long fournisseurId) {
        Boolean sousSeuilBool = null;
        if (!sousSeuil.isEmpty()) {
            sousSeuilBool = Boolean.parseBoolean(sousSeuil);
        }
        return ResponseEntity.ok(

                produitService.getProduitsFiltres(nom, sousSeuilBool, fournisseurId, page, size)
        );
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

    // 👉 Endpoint pour récupérer tous les produits avec leur id et nom
    @GetMapping("/nom-id")
    public List<ProduitNomIdDTO> getAllProduitNomId() {
        return produitService.getAllProduitNomId();
    }
}
