package org.jad.auth.util;

import jakarta.persistence.criteria.Predicate;
import org.jad.auth.entity.Produit;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProduitSpecifications {

    public static Specification<Produit> filtrerProduits(String nom, Boolean sousSeuil, Long fournisseurId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (nom != null && !nom.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("nom")), "%" + nom.toLowerCase() + "%"));
            }

            if (sousSeuil != null && sousSeuil) {
                predicates.add(cb.lessThanOrEqualTo(root.get("quantiteStock"), root.get("reorderPoint")));
            }

            if (fournisseurId != null) {
                predicates.add(cb.equal(root.get("fournisseur").get("id"), fournisseurId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
