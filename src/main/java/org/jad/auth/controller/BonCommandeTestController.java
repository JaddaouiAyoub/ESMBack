package org.jad.auth.controller;

import lombok.RequiredArgsConstructor;
import org.jad.auth.dto.CommandeDTO;
import org.jad.auth.dto.FournisseurDTO;
import org.jad.auth.dto.FournisseurDTOFC;
import org.jad.auth.dto.LigneCommandeDTO;
import org.jad.auth.service.BonCommandePdfService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pdf")
@RequiredArgsConstructor
public class BonCommandeTestController {

    private final BonCommandePdfService pdfService;

    @GetMapping("/test")
    public ResponseEntity<FileSystemResource> testPdfGeneration() {
        // 🧪 Simuler une commande
        FournisseurDTOFC fournisseur = new FournisseurDTOFC();
        fournisseur.setId(1L);
        fournisseur.setCode("F01");
        fournisseur.setRaisonSociale("Fournisseur Maroc");

        LigneCommandeDTO ligne1 = new LigneCommandeDTO("Clavier Logitech", 10, 120.0, LocalDate.now().plusDays(5));
        LigneCommandeDTO ligne2 = new LigneCommandeDTO("Souris Dell", 5, 80.0, LocalDate.now().plusDays(5));

        CommandeDTO commande = new CommandeDTO();
        commande.setId(1L);
        commande.setCodeCommande("4501");
        commande.setDateCreation(LocalDate.now().toString());
        commande.setStatut("CREEE");
        commande.setMontantTotal(1600.0);
        commande.setFournisseur(fournisseur);
        commande.setLignes(List.of(ligne1, ligne2));

        // 📄 Générer le PDF
        Map<String, Object> model = Map.of("commande", commande);
        File pdfFile = pdfService.genererPdfCommande(model, "commande_4501_test");

        // 📤 Retourner le fichier pour téléchargement
        FileSystemResource resource = new FileSystemResource(pdfFile);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=bon_commande_test.pdf")
                .contentLength(pdfFile.length())
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
}
