package org.jad.auth.service;

import lombok.RequiredArgsConstructor;
import org.jad.auth.dto.ProduitDTO;
import org.jad.auth.dto.ProduitNomIdDTO;
import org.jad.auth.entity.Fournisseur;
import org.jad.auth.entity.LigneCommande;
import org.jad.auth.entity.Produit;
import org.jad.auth.exception.RessourceNotFoundException;
import org.jad.auth.mapper.ProduitMapper;
import org.jad.auth.repository.FournisseurRepository;
import org.jad.auth.repository.LigneCommandeRepository;
import org.jad.auth.repository.ProduitRepository;
import org.jad.auth.util.ProduitSpecifications;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProduitService {

    private final ProduitRepository produitRepository;
    private final FournisseurRepository fournisseurRepository;
    private final CommandeService commandeService;
    private final LigneCommandeRepository ligneCommandeRepository;
    // ➤ Créer un nouveau produit (sans fournisseur au départ)
    public ProduitDTO createProduit(ProduitDTO produitDTO) {
        Produit produit = ProduitMapper.toEntity(produitDTO, null);
        Produit savedProduit = produitRepository.save(produit);
        return ProduitMapper.toDTO(savedProduit);
    }

    // ➤ Mettre à jour un produit existant
    public ProduitDTO updateProduit(Long produitId, ProduitDTO updatedProduitDTO) {
        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new RessourceNotFoundException("Produit non trouvé"));

        produit.setNom(updatedProduitDTO.getNom());
        produit.setQuantiteStock(updatedProduitDTO.getQuantiteStock());
        produit.setQuantiteVendu(updatedProduitDTO.getQuantiteVendu());
        produit.setReorderPoint(updatedProduitDTO.getReorderPoint());
        produit.setPrix(updatedProduitDTO.getPrix());
        produit.setLeadTime(updatedProduitDTO.getLeadTime());
        produit.setStockInitiale(updatedProduitDTO.getStockInitiale());
        // pas besoin de toucher au fournisseur ici
        Produit savedProduit = produitRepository.save(produit);
        return ProduitMapper.toDTO(savedProduit);
    }

    // ➤ Supprimer un produit
    public void deleteProduit(Long produitId) {
        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new RessourceNotFoundException("Produit non trouvé"));
        // Vérifier si le produit a un fournisseur associé
        if (produit.getFournisseur() != null) {
            // Si oui, on le dissocie
            Fournisseur fournisseur = produit.getFournisseur();
            produit.setFournisseur(null);
            fournisseur.getProduits().remove(produit);
            fournisseurRepository.save(fournisseur);
        }
        List<LigneCommande> lignes = ligneCommandeRepository.findByProduit(produit);
        for (LigneCommande ligne : lignes) {
            ligneCommandeRepository.delete(ligne);
        }
        produitRepository.delete(produit);
    }

    // ➤ Trouver un produit par ID
    public ProduitDTO getProduit(Long produitId) {
        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new RessourceNotFoundException("Produit non trouvé"));
        return ProduitMapper.toDTO(produit);
    }

    // ➤ Lister tous les produits
    public List<ProduitDTO> getAllProduits() {
        return produitRepository.findAll()
                .stream()
                .map(ProduitMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ➤ Lister les produits d'un fournisseur donné
    public List<ProduitDTO> getProduitsByFournisseur(Long fournisseurId) {
        return produitRepository.findByFournisseurId(fournisseurId)
                .stream()
                .map(ProduitMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ➤ Affecter un produit à un fournisseur (spécial)
    public ProduitDTO affecterProduitAFournisseur(Long produitId, Long fournisseurId) {
        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new RessourceNotFoundException("Produit non trouvé"));

        Fournisseur fournisseur = fournisseurRepository.findById(fournisseurId)
                .orElseThrow(() -> new RessourceNotFoundException("Fournisseur non trouvé"));

        produit.setFournisseur(fournisseur);
        Produit savedProduit = produitRepository.save(produit);
        // 🔁 Si le stock est nul, on génère une commande automatique
        if (produit.getQuantiteStock() <= produit.getReorderPoint()) {
            commandeService.creerCommandeAutomatique(produit, fournisseur);
        }
        return ProduitMapper.toDTO(savedProduit);
    }

    // ➤ Trouver tous les produits sous le seuil ROP
    public List<ProduitDTO> getProduitsSousSeuil() {
        return produitRepository.findAll()
                .stream()
                .filter(p -> p.getQuantiteStock() < p.getReorderPoint())
                .map(ProduitMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Page<ProduitDTO> getProduitsAvecPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return produitRepository.findAll(pageable).map(ProduitMapper::toDTO);
    }
    public Page<ProduitDTO> getProduitsFiltres(String nom, Boolean sousSeuil, Long fournisseurId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Specification<Produit> spec = ProduitSpecifications.filtrerProduits(nom, sousSeuil, fournisseurId);
        return produitRepository.findAll(spec, pageable).map(ProduitMapper::toDTO);
    }



    // 👉 Méthode pour récupérer les noms et ids de tous les produits
    public List<ProduitNomIdDTO> getAllProduitNomId() {
        return produitRepository.findAllNomId(); // Cette méthode doit être définie dans le repository
    }

}
