package org.jad.auth.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jad.auth.dto.TopProduitVenduProjection;
import org.jad.auth.dto.VenteRequestDTO;
import org.jad.auth.dto.VenteResponseDTO;
import org.jad.auth.entity.Fournisseur;
import org.jad.auth.entity.LigneCommande;
import org.jad.auth.entity.Produit;
import org.jad.auth.entity.Vente;
import org.jad.auth.repository.LigneCommandeRepository;
import org.jad.auth.repository.ProduitRepository;
import org.jad.auth.repository.VenteRepository;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VenteServiceImpl implements VenteService {

    private final VenteRepository venteRepository;
    private final ProduitRepository produitRepository;
    private final LigneCommandeRepository ligneCommandeRepository;
    private final CommandeService commandeService;
    @Override
    @Transactional
    public VenteResponseDTO enregistrerVente(VenteRequestDTO dto) {
        Produit produit = produitRepository.findById(dto.getProduitId())
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));

        // Mise à jour du stock
        if (produit.getQuantiteStock() < dto.getQuantite()) {
            throw new RuntimeException("Stock insuffisant");
        }

        produit.setQuantiteStock(produit.getQuantiteStock() - dto.getQuantite());
        produit.setQuantiteVendu(produit.getQuantiteVendu() + dto.getQuantite());
        produit.setReorderPoint(calculerROP(produit.getId()));
        produitRepository.save(produit);
        Fournisseur fournisseur = produit.getFournisseur();
        if(produit.getQuantiteStock()<produit.getReorderPoint()){
            commandeService.creerCommandeAutomatique(produit, fournisseur);
        }
        Vente vente = Vente.builder()
                .produit(produit)
                .quantiteVendue(dto.getQuantite())
                .prixVenteUnitaire(dto.getPrixVenteUnitaire())
                .dateVente(LocalDate.now())
                .build();

        vente = venteRepository.save(vente);

        return VenteResponseDTO.builder()
                .id(vente.getId())
                .nomProduit(produit.getNom())
                .quantiteVendue(vente.getQuantiteVendue())
                .prixVenteUnitaire(vente.getPrixVenteUnitaire())
                .dateVente(vente.getDateVente())
                .build();
    }

    @Override
    public Page<VenteResponseDTO> rechercherVentes(Optional<String> nomProduit, Optional<LocalDate> dateDebut, Optional<LocalDate> dateFin, Pageable pageable) {
        Specification<Vente> spec = Specification.where(null);

        if (nomProduit.isPresent()) {
            spec = spec.and((root, query, cb) ->
                cb.like(cb.lower(root.get("produit").get("nom")), "%" + nomProduit.get().toLowerCase() + "%"));
        }

        if (dateDebut.isPresent() && dateFin.isPresent()) {
            LocalDate debut = dateDebut.get();
            LocalDate fin = dateFin.get();
            spec = spec.and((root, query, cb) ->
                cb.between(root.get("dateVente"), debut, fin));
        }

        // Ajout du tri par défaut si non fourni
        Sort sort = pageable.getSort().isSorted() ? pageable.getSort() : Sort.by(Sort.Direction.DESC, "dateVente");
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        return venteRepository.findAll(spec, sortedPageable).map(v ->
                VenteResponseDTO.builder()
                        .id(v.getId())
                        .nomProduit(v.getProduit().getNom())
                        .quantiteVendue(v.getQuantiteVendue())
                        .prixVenteUnitaire(v.getPrixVenteUnitaire())
                        .dateVente(v.getDateVente())
                        .build());
    }
    @Override
    public VenteResponseDTO getVenteById(Long id) {
        Vente vente = venteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vente non trouvée avec l'id: " + id));

        return VenteResponseDTO.builder()
                .id(vente.getId())
                .nomProduit(vente.getProduit().getNom())
                .quantiteVendue(vente.getQuantiteVendue())
                .prixVenteUnitaire(vente.getPrixVenteUnitaire())
                .dateVente(vente.getDateVente())
                .build(); // méthode déjà utilisée pour transformer une entité en DTO
    }

    @Override
    public void supprimerVente(Long id) {
        Vente vente = venteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vente non trouvée avec l'id: " + id));

        // Optionnel : remettre le stock au produit si nécessaire
        Produit produit = vente.getProduit();
        produit.setQuantiteVendu(produit.getQuantiteVendu() - vente.getQuantiteVendue());
        produit.setQuantiteStock(produit.getQuantiteStock() + vente.getQuantiteVendue());
        produitRepository.save(produit);

        venteRepository.delete(vente);
    }


//    public int calculerROP(Long produitId) {
//        // Étape 1 : Récupérer les ventes du produit
//        List<Vente> ventes = venteRepository.findByProduitId(produitId);
//
//        if (ventes.isEmpty()) {
//
//            return 0;
//        }
//
//        // Étape 2 : Filtrer les ventes des 30 derniers jours si des données plus anciennes existent
//        LocalDate now = LocalDate.now();
//        boolean contientAnciennesVentes = ventes.stream()
//                .anyMatch(v -> ChronoUnit.DAYS.between(v.getDateVente(), now) > 30);
//
//        List<Vente> ventesFiltrees = contientAnciennesVentes
//                ? ventes.stream().filter(v -> ChronoUnit.DAYS.between(v.getDateVente(), now) <= 30).toList()
//                : ventes;
//
//        // Étape 3 : Grouper les ventes par jour
//        Map<LocalDate, Integer> ventesParJour = ventesFiltrees.stream()
//                .collect(Collectors.groupingBy(
//                        Vente::getDateVente,
//                        Collectors.summingInt(Vente::getQuantiteVendue)
//                ));
//
//        int nbJours = ventesParJour.size();
//        double demandeMoyenneParJour = nbJours > 0
//                ? ventesParJour.values().stream().mapToInt(i -> i).sum() / (double) nbJours
//                : 0;
//
//        // Étape 4 : Calcul du lead time max à partir des lignes de commande
//        List<LigneCommande> lignes = ligneCommandeRepository.findByProduitId(produitId);
//
//        List<Long> leadTimes = lignes.stream()
//                .filter(lc -> lc.getDateDexpeditionConfirmee() != null && lc.getDateReception() != null)
//                .map(lc -> ChronoUnit.DAYS.between(lc.getDateDexpeditionConfirmee(), lc.getDateReception()))
//                .filter(d -> d > 0)
//                .toList();
//
//        long leadTimeMax = leadTimes.stream().mapToLong(Long::longValue).max().orElse(1);
//
//        // Étape 5 : Calcul du ROP = demande moyenne × lead time max
//        return (int) Math.round(demandeMoyenneParJour * leadTimeMax);
//
//    }

    public int calculerROP(Long produitId) {
        // 1. Récupérer les ventes du produit
        List<Vente> ventes = venteRepository.findByProduitId(produitId);
        if (ventes.isEmpty()) return 0;

        LocalDate now = LocalDate.now();
        boolean contientAnciennesVentes = ventes.stream()
                .anyMatch(v -> ChronoUnit.DAYS.between(v.getDateVente(), now) > 30);

        List<Vente> ventesFiltrees = contientAnciennesVentes
                ? ventes.stream().filter(v -> ChronoUnit.DAYS.between(v.getDateVente(), now) <= 30).toList()
                : ventes;

        // 2. Grouper les ventes par jour
        Map<LocalDate, Integer> ventesParJour = ventesFiltrees.stream()
                .collect(Collectors.groupingBy(
                        Vente::getDateVente,
                        Collectors.summingInt(Vente::getQuantiteVendue)
                ));

        int nbJours = ventesParJour.size();
        if (nbJours == 0) return 0;

        // Demande moyenne & max
        List<Integer> demandes = new ArrayList<>(ventesParJour.values());
        double demandeMoyenne = demandes.stream().mapToInt(i -> i).average().orElse(0);
        int demandeMax = demandes.stream().mapToInt(i -> i).max().orElse(0);

        // 3. Calcul du lead time à partir des lignes de commande
        List<LigneCommande> lignes = ligneCommandeRepository.findByProduitId(produitId);
        List<Long> leadTimes = lignes.stream()
                .filter(lc -> lc.getDateDexpeditionConfirmee() != null && lc.getDateReception() != null)
                .map(lc -> ChronoUnit.DAYS.between(lc.getDateDexpeditionConfirmee(), lc.getDateReception()))
                .filter(d -> d > 0)
                .toList();

        double leadTimeMoyen = leadTimes.stream().mapToLong(Long::longValue).average().orElse(1);
        long leadTimeMax = leadTimes.stream().mapToLong(Long::longValue).max().orElse(1);

        // 4. Calcul du Stock de Sécurité
        double stockSecurite = (demandeMax * leadTimeMax) - (demandeMoyenne * leadTimeMoyen);

        // 5. Calcul du ROP
        double rop = (demandeMoyenne * leadTimeMoyen) + stockSecurite;

        return (int) Math.round(rop);
    }

    @Override
    public List<Map<String, Object>> getTopProduitsVendus() {
        List<TopProduitVenduProjection> topProduits = venteRepository.findTopProduitsVendus();

        return topProduits.stream().map(produit -> {
            Map<String, Object> map = new HashMap<>();
            map.put("name", produit.getProduitNom());
            map.put("value", produit.getTotalVendu());
            return map;
        }).collect(Collectors.toList());
    }


}
