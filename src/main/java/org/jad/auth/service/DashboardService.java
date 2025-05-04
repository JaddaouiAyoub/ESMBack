package org.jad.auth.service;

import lombok.RequiredArgsConstructor;
import org.jad.auth.dto.DashboardDTO;
import org.jad.auth.entity.Commande;
import org.jad.auth.entity.LigneCommande;
import org.jad.auth.enums.StatutCommande;
import org.jad.auth.repository.CommandeRepository;
import org.jad.auth.repository.ProduitRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final CommandeRepository commandeRepository;
    private final ProduitRepository produitRepository;

    public DashboardDTO getDashboardData() {
        List<Commande> commandes = commandeRepository.findAll();

        long total = commandes.size();
        long enCours = commandes.stream().filter(c -> c.getStatut() == StatutCommande.EN_COURS).count();
        long confirmee = commandes.stream().filter(c -> c.getStatut() == StatutCommande.CONFIRMEE).count();
        long partiel = commandes.stream().filter(c -> c.getStatut() == StatutCommande.PARTIELLEMENT_RECUE).count();
        long recues = commandes.stream().filter(c -> c.getStatut() == StatutCommande.RECUE).count();
        long enRetard = commandes.stream().filter(c ->
                c.getStatut() != StatutCommande.RECUE &&
                        c.getLignesCommande().stream().anyMatch(lc ->
                                lc.getDateLivraisonPrevue() != null &&
                                lc.getDateLivraisonPrevue().isBefore(LocalDate.now())
                        )
        ).count();

        double montantTotal = commandes.stream()
                .flatMap(c -> c.getLignesCommande().stream())
                .mapToDouble(l -> l.getPrixUnitaire() * l.getQuantiteCommandee())
                .sum();

        long produitsSousSeuil = produitRepository.findAll().stream()
                .filter(p -> p.getQuantiteStock() < p.getReorderPoint())
                .count();

        Map<String, Long> parStatut = Arrays.stream(StatutCommande.values())
                .collect(Collectors.toMap(
                        Enum::name,
                        s -> commandes.stream().filter(c -> c.getStatut() == s).count()
                ));

        Map<String, Long> parFournisseur = commandes.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getFournisseur().getRaisonSociale(),
                        Collectors.counting()
                ));

        Map<String, Long> topProduits = commandes.stream()
                .flatMap(c -> c.getLignesCommande().stream())
                .collect(Collectors.groupingBy(
                        l -> l.getProduit().getNom(),
                        Collectors.summingLong(LigneCommande::getQuantiteCommandee)
                ));

        // Tri du top 5
        topProduits = topProduits.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));

        return DashboardDTO.builder()
                .totalCommandes(total)
                .commandesEnCours(enCours)
                .commandesPartiellementRecues(partiel)
                .commandesRecues(recues)
                .commandesConfirmees(confirmee)
                .commandesEnRetard(enRetard)
                .montantTotalCommandes(montantTotal)
                .produitsSousSeuil(produitsSousSeuil)
                .commandesParStatut(parStatut)
                .commandesParFournisseur(parFournisseur)
                .topProduitsCommandes(topProduits)
                .build();
    }
}
