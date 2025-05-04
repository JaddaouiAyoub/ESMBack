package org.jad.auth.service;

import jakarta.transaction.Transactional;
import org.jad.auth.dto.CommandeDTO;
import org.jad.auth.dto.LigneCommandeModificationDTO;
import org.jad.auth.entity.*;
import org.jad.auth.enums.StatutCommande;
import org.jad.auth.repository.CommandeRepository;
import org.jad.auth.repository.LigneCommandeRepository;
import org.jad.auth.repository.SousLigneCommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommandeService {

    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private LigneCommandeRepository ligneCommandeRepository;

    @Autowired
    private BonCommandePdfService bonCommandePdfService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private SousLigneCommandeRepository sousLigneCommandeRepository;
    /**
     * Crée automatiquement une commande pour un produit à stock nul
     */
    public Commande creerCommandeAutomatique(Produit produit, Fournisseur fournisseur) {
        // Génère le code : exemple 4501, 4502, etc.
        String codeCommande = generateCommandeCode();

        Commande commande = Commande.builder()
                .codeCommande(codeCommande)
                .dateCreation(LocalDate.now())
                .fournisseur(fournisseur)
                .statut(StatutCommande.CREEE)
                .montantTotal(produit.getPrix() * produit.getReorderPoint())
                .build();

        LigneCommande ligne = LigneCommande.builder()
                .commande(commande)
                .produit(produit)
                .quantiteCommandee(produit.getReorderPoint())
                .prixUnitaire(produit.getPrix())
                .dateLivraisonPrevue(LocalDate.now().plusDays(7))
                .build();

        commande.setLignesCommande(Collections.singletonList(ligne));

        // Sauvegarder d'abord la commande pour générer l'ID
        commande = commandeRepository.save(commande);

        ligne.setCommande(commande);
        ligneCommandeRepository.save(ligne);

        return commande;
    }

    /**
     * Récupère toutes les commandes avec statut CREEE
     */
    public List<CommandeDTO> getCommandesCreeesDTO() {
        return commandeRepository.findByStatut(StatutCommande.CREEE)
                .stream()
                .map(CommandeDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Récupère toutes les commandes avec statut EN_COURS
     */
    public List<CommandeDTO> getCommandesEnCoursDTO() {
        return commandeRepository.findByStatut(StatutCommande.EN_COURS)
                .stream()
                .map(CommandeDTO::new)
                .collect(Collectors.toList());
    }
    /**
     * Récupère toutes les commandes avec statut CONFIRMEE
     */
    public List<CommandeDTO> getCommandesConfirmeesDTO() {
        return commandeRepository.findByStatut(StatutCommande.CONFIRMEE)
                .stream()
                .map(CommandeDTO::new)
                .collect(Collectors.toList());
    }


    /**
     * Permet de modifier les lignes d'une commande et de passer le statut à EN_COURS
     */
    public void modifierCommande(Long commandeId, List<LigneCommandeModificationDTO> lignesModifiees) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));

//        if (commande.getStatut() != StatutCommande.CREEE) {
//            throw new RuntimeException("La commande ne peut plus être modifiée.");
//        }
        commande.setStatut(StatutCommande.EN_COURS);

        for (LigneCommandeModificationDTO ligneModifiee : lignesModifiees) {
            LigneCommande ligne = ligneCommandeRepository.findById(ligneModifiee.getId())
                    .orElseThrow(() -> new RuntimeException("Ligne commande non trouvée"));

            ligne.setQuantiteCommandee(ligneModifiee.getQuantiteCommandee());
            ligne.setDateLivraisonPrevue(ligneModifiee.getDateLivraisonPrevue());
            ligne.setDateDexpeditionConfirmee(ligneModifiee.getDateDexpeditionConfirmee());
            ligne.setPrixUnitaire(ligneModifiee.getPrixUnitaire());
            // Si la date d'expédition est confirmée, on peut mettre à jour le statut de la commande
            if (ligneModifiee.getDateDexpeditionConfirmee() != null) {
                commande.setStatut(StatutCommande.CONFIRMEE);
            }
            ligneCommandeRepository.save(ligne);
        }
        double nouveauMontantTotal = commande.getLignesCommande().stream()
                .mapToDouble(l -> l.getQuantiteCommandee() * l.getPrixUnitaire())
                .sum();

        commande.setMontantTotal(nouveauMontantTotal);


        commandeRepository.save(commande);

        // ✅ 1. Générer le PDF
//        CommandeDTO commandeDTO = new CommandeDTO(commande); // ou manuellement si pas de mapper
//        Map<String, Object> model = Map.of("commande", commandeDTO);
//        File pdfFile = bonCommandePdfService.genererPdfCommande(model, "commande_" + commande.getCodeCommande());
//        // ✅ 2. Envoyer le PDF par email (non implémenté ici)
//        String fournisseurEmail = commande.getFournisseur().getEmail(); // Assure-toi que l'email existe dans l'entité Fournisseur
//        String sujet = "Bon de commande mis à jour";
//        String message = "Bonjour,\n\nVeuillez trouver ci-joint le bon de commande mis à jour.\n\nCordialement,\nEasyStock Maroc";
//        emailService.envoyerEmailAvecPieceJointe(fournisseurEmail, sujet, message, pdfFile);
        // ✅ 3. Supprimer le fichier temporaire
//        if (pdfFile.exists()) {
//            pdfFile.delete();
//        }
    }

    @Transactional
    public void enregistrerReception(Long ligneCommandeId, int quantiteRecue, LocalDate dateReception) {
        LigneCommande ligne = ligneCommandeRepository.findById(ligneCommandeId)
                .orElseThrow(() -> new RuntimeException("LigneCommande introuvable"));

        if (quantiteRecue <= 0 || quantiteRecue > ligne.getQuantiteCommandee()) {
            throw new IllegalArgumentException("Quantité reçue invalide.");
        }

        // Créer une sous-ligne si la quantité est partielle
        if (quantiteRecue < ligne.getQuantiteCommandee()) {
            SousLigneCommande sousLigne = SousLigneCommande.builder()
                    .quantiteRecue(quantiteRecue)
                    .dateReception(dateReception)
                    .ligneCommande(ligne)
                    .build();
            // Vérifie si toutes les lignes sont reçues pour clôturer la commande
            Commande commande = ligne.getCommande();
            commande.setStatut(StatutCommande.PARTIELLEMENT_RECUE);
            sousLigneCommandeRepository.save(sousLigne);
        }

        // Si la quantité est complètement reçue, mettre à jour la ligne
        if (quantiteRecue == ligne.getQuantiteCommandee()) {
            ligne.setDateReception(dateReception);
            ligneCommandeRepository.save(ligne);

            // Vérifie si toutes les lignes sont reçues pour clôturer la commande
            Commande commande = ligne.getCommande();
            boolean toutesRecues = commande.getLignesCommande().stream()
                    .allMatch(l -> l.getDateReception() != null ||
                            (l.getSousLignes() != null && !l.getSousLignes().isEmpty()));
            if (toutesRecues) {
                commande.setStatut(StatutCommande.RECUE);
                commandeRepository.save(commande);
            }
        }
    }


    /**
     * Génère un code de commande unique type "4501"
     */
    private String generateCommandeCode() {
        long count = commandeRepository.count() + 1;
        return "45" + String.format("%02d", count);
    }
}
