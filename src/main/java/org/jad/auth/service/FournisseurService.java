package org.jad.auth.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jad.auth.dto.FournisseurDTO;
import org.jad.auth.entity.Fournisseur;
import org.jad.auth.entity.FournisseurCodeCounter;
import org.jad.auth.entity.RoleEntity;
import org.jad.auth.exception.RessourceNotFoundException;
import org.jad.auth.repository.FournisseurCodeCounterRepository;
import org.jad.auth.repository.FournisseurRepository;
import org.jad.auth.repository.RoleRepository;
import org.jad.auth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FournisseurService {

    private final FournisseurRepository fournisseurRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final FournisseurCodeCounterRepository fournisseurCodeCounterRepository;
    // ➤ Créer un nouveau Fournisseur
    @Transactional
    public FournisseurDTO createFournisseur(FournisseurDTO fournisseurDTO) {
        // Création d'une instance Fournisseur
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setUsername(fournisseurDTO.getUsername());
        fournisseur.setEmail(fournisseurDTO.getEmail());
//        fournisseur.setFirstName(fournisseurDTO.getFirstName());
//        fournisseur.setLastName(fournisseurDTO.getLastName());
        fournisseur.setRaisonSociale(fournisseurDTO.getRaisonSociale());
        fournisseur.setPhoneNumber(fournisseurDTO.getPhoneNumber());
        fournisseur.setPassword(passwordEncoder.encode("123456"));  // Mot de passe par défaut sécurisé

        // Affecter le rôle "FOURNISSEUR"
        RoleEntity role = roleRepository.findByName("FOURNISSEUR");
        if (role == null) {
            throw new RessourceNotFoundException("Rôle 'FOURNISSEUR' non trouvé");
        }
        fournisseur.setRole(role);

// Vérifie si le compteur existe, sinon crée-le
        FournisseurCodeCounter counter = fournisseurCodeCounterRepository.findById(1L).orElseGet(() -> {
            FournisseurCodeCounter newCounter = new FournisseurCodeCounter();
            newCounter.setLastUsedCode(0);  // Initialisation à 0 si le compteur n'existe pas
            return fournisseurCodeCounterRepository.save(newCounter);
        });        int newCodeNumber = counter.getLastUsedCode() + 1;
        String code = String.format("F%04d", newCodeNumber); // Format avec 4 chiffres

        // Affectation du code au fournisseur
        fournisseur.setCode(code);

        // Sauvegarde du fournisseur
        Fournisseur savedFournisseur = fournisseurRepository.save(fournisseur);

        // Mise à jour du compteur
        counter.setLastUsedCode(newCodeNumber);
        fournisseurCodeCounterRepository.save(counter);

        System.out.println("Fournisseur créé avec succès : " + savedFournisseur.getUsername());

        // Retourner le DTO
        return mapToDTO(savedFournisseur);
    }




    // ➤ Récupérer tous les fournisseurs
    public List<FournisseurDTO> getAllFournisseurs() {
        return fournisseurRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ➤ Récupérer un seul fournisseur
    public FournisseurDTO getFournisseur(Long id) {
        Fournisseur fournisseur = fournisseurRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Fournisseur non trouvé"));
        return mapToDTO(fournisseur);
    }

    // ➤ Supprimer un fournisseur
    public void deleteFournisseur(Long id) {
        Fournisseur fournisseur = fournisseurRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Fournisseur non trouvé"));
        // Vérifier si le fournisseur a des produits associés
        if (!fournisseur.getProduits().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer le fournisseur, il a des produits associés.");
        }
        if (!fournisseur.getCommandes().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer le fournisseur, il a des commandes associées.");
        }
        fournisseurRepository.delete(fournisseur);
    }

    // ➤ Méthode utilitaire : convertir Fournisseur vers DTO


    public FournisseurDTO updateFournisseur(Long id, FournisseurDTO fournisseurDTO) {
        Fournisseur fournisseur = fournisseurRepository.findById(id).orElseThrow(() -> new RessourceNotFoundException("Fournisseur non trouvé"));

        // Mettre à jour les informations du fournisseur
        fournisseur.setUsername(fournisseurDTO.getUsername());
        fournisseur.setEmail(fournisseurDTO.getEmail());
//        fournisseur.setFirstName(fournisseurDTO.getFirstName());
//        fournisseur.setLastName(fournisseurDTO.getLastName());
        fournisseur.setRaisonSociale(fournisseurDTO.getRaisonSociale());
        fournisseur.setPhoneNumber(fournisseurDTO.getPhoneNumber());

        // Sauvegarder les changements dans la base de données
        fournisseur = fournisseurRepository.save(fournisseur);

        // Retourner le fournisseur mis à jour sous forme de DTO
        return mapToDTO(fournisseur);
    }
    private FournisseurDTO mapToDTO(Fournisseur fournisseur) {
        FournisseurDTO dto = new FournisseurDTO();
        dto.setId(fournisseur.getId());
        dto.setUsername(fournisseur.getUsername());
        dto.setEmail(fournisseur.getEmail());
//        dto.setFirstName(fournisseur.getFirstName());
//        dto.setLastName(fournisseur.getLastName());
        dto.setRaisonSociale(fournisseur.getRaisonSociale());
        dto.setPhoneNumber(fournisseur.getPhoneNumber());
        dto.setCode(fournisseur.getCode());
        return dto;
    }
}
