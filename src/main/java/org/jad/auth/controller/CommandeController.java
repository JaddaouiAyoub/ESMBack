package org.jad.auth.controller;

import org.jad.auth.dto.CommandeDTO;
import org.jad.auth.dto.LigneCommandeModificationDTO;
import org.jad.auth.dto.ReceptionRequest;
import org.jad.auth.entity.Commande;
import org.jad.auth.entity.Fournisseur;
import org.jad.auth.entity.LigneCommande;
import org.jad.auth.entity.Produit;
import org.jad.auth.repository.FournisseurRepository;
import org.jad.auth.repository.ProduitRepository;
import org.jad.auth.service.CommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commandes")
public class CommandeController {

    @Autowired
    private CommandeService commandeService;

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private FournisseurRepository fournisseurRepository;

    /**
     * Déclenche une commande automatique si stock == 0
     */
    @PostMapping("/auto")
    public Commande commanderSiStockVide(@RequestParam Long produitId, @RequestParam Long fournisseurId) {
        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        Fournisseur fournisseur = fournisseurRepository.findById(fournisseurId)
                .orElseThrow(() -> new RuntimeException("Fournisseur non trouvé"));

        if (produit.getQuantiteStock() > 0) {
            throw new RuntimeException("Stock disponible, pas besoin de commander.");
        }

        return commandeService.creerCommandeAutomatique(produit, fournisseur);
    }

    /**
     * GET : Commandes au statut CREEE
     */
    @GetMapping("/creees")
    public List<CommandeDTO> getCommandesCreees() {
        return commandeService.getCommandesCreeesDTO();
    }

    /**
     * GET : Commandes au statut EN_COURS
     */
    @GetMapping("/encours")
    public List<CommandeDTO> getCommandesEnCours() {
        return commandeService.getCommandesEnCoursDTO();
    }
    /**
     * GET : Commandes au statut CONFIRMEE
     */
    @GetMapping("/confirmees")
    public List<CommandeDTO> getCommandesConfirmees() {
        return commandeService.getCommandesConfirmeesDTO();
    }

    /**
     * PUT : Modifier les lignes d'une commande (quantité, date) et passer en EN_COURS
     */
    @PutMapping("/{commandeId}/modifier")
    public ResponseEntity<String> modifierCommande(
            @PathVariable Long commandeId,
            @RequestBody List<LigneCommandeModificationDTO> lignes
    ) {
        try {
            commandeService.modifierCommande(commandeId, lignes);
            return ResponseEntity.ok("Commande modifiée avec succès");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur inattendue est survenue");
        }
    }
    @PostMapping("/lignes/{id}/reception")
    public ResponseEntity<?> receptionnerLigneCommande(
            @PathVariable Long id,
            @RequestBody ReceptionRequest request) {
        commandeService.enregistrerReception(id, request.getQuantiteRecue(), request.getDateReception());
        return ResponseEntity.ok().build();
    }


}
