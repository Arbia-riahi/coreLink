
package com.ProjectManagement.repositories;
import com.ProjectManagement.entities.EProjet;
import com.ProjectManagement.entities.Projet;
import org.springframework.data.jpa.domain.Specification;


import java.time.LocalDate;

public class ProjetSpecifications {

    public static Specification<Projet> archived(Boolean archived) {
        return (root, q, cb) -> archived == null ? cb.conjunction() : cb.equal(root.get("archived"), archived);
    }

    public static Specification<Projet> titreContains(String titre) {
        return (root, q, cb) -> (titre == null || titre.isBlank())
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("titre")), "%" + titre.toLowerCase() + "%");
    }

    public static Specification<Projet> descriptionContains(String desc) {
        return (root, q, cb) -> (desc == null || desc.isBlank())
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("description")), "%" + desc.toLowerCase() + "%");
    }

    public static Specification<Projet> statusEquals(String status) {
        return (root, q, cb) -> {
            if (status == null || status.isBlank()) return cb.conjunction();
            return cb.equal(root.get("status"), EProjet.valueOf(status));
        };
    }

    public static Specification<Projet> dateDebutBetween(LocalDate from, LocalDate to) {
        return (root, q, cb) -> {
            if (from == null && to == null) return cb.conjunction();
            if (from != null && to != null) return cb.between(root.get("dateDebut"), from, to);
            if (from != null) return cb.greaterThanOrEqualTo(root.get("dateDebut"), from);
            return cb.lessThanOrEqualTo(root.get("dateDebut"), to);
        };
    }

    public static Specification<Projet> dateFinBetween(LocalDate from, LocalDate to) {
        return (root, q, cb) -> {
            if (from == null && to == null) return cb.conjunction();
            if (from != null && to != null) return cb.between(root.get("dateFin"), from, to);
            if (from != null) return cb.greaterThanOrEqualTo(root.get("dateFin"), from);
            return cb.lessThanOrEqualTo(root.get("dateFin"), to);
        };
    }

    /** hasFiles = true => fichiers non vides */
    public static Specification<Projet> hasFiles(Boolean hasFiles) {
        return (root, q, cb) -> {
            if (hasFiles == null) return cb.conjunction();
            return hasFiles ? cb.isNotEmpty(root.get("fichiers")) : cb.isEmpty(root.get("fichiers"));
        };
    }

    public static Specification<Projet> chefMatriculeEquals(String matriculeChef) {
        return (root, q, cb) -> (matriculeChef == null || matriculeChef.isBlank())
                ? cb.conjunction()
                : cb.equal(root.get("chefProjet").get("matricule"), matriculeChef);
    }

    public static Specification<Projet> membreMatriculeEquals(String matriculeMembre) {
        return (root, q, cb) -> {
            if (matriculeMembre == null || matriculeMembre.isBlank()) return cb.conjunction();
            var membres = root.join("membres", jakarta.persistence.criteria.JoinType.LEFT);
            q.distinct(true);
            return cb.equal(membres.get("matricule"), matriculeMembre);
        };
    }

   
    
 // ProjetSpecifications.java (en haut du fichier)
    private static String likeize(String raw) {
        // échappe % et _ (wildcards SQL)
        String s = raw.toLowerCase()
                      .replace("\\", "\\\\")
                      .replace("%", "\\%")
                      .replace("_", "\\_");
        return "%" + s + "%";
    }
 // ProjetSpecifications.java
    public static Specification<Projet> q(String qStr) {
        return (root, query, cb) -> {
            if (qStr == null || qStr.isBlank()) return cb.conjunction();
            String like = likeize(qStr);

            // Texte classiques
            var titre      = cb.lower(root.get("titre"));
            var desc       = cb.lower(root.get("description"));
            var chefNom    = cb.lower(root.get("chefProjet").get("nom"));
            var chefPrenom = cb.lower(root.get("chefProjet").get("prenom"));
            var statusName = cb.lower(root.get("status").as(String.class));
            var chefMatric = cb.lower(root.get("chefProjet").get("matricule"));
            var numeroStr  = cb.lower(cb.concat(root.get("numeroProjet").as(String.class), "")); // cast string

            // Dates formatées "YYYY-MM-DD"
            var debStr = cb.function("DATE_FORMAT", String.class, root.get("dateDebut"), cb.literal("%Y-%m-%d"));
            var finStr = cb.function("DATE_FORMAT", String.class, root.get("dateFin"),   cb.literal("%Y-%m-%d"));

            return cb.or(
                cb.like(titre, like, '\\'),
                cb.like(desc, like, '\\'),
                cb.like(chefNom, like, '\\'),
                cb.like(chefPrenom, like, '\\'),
                cb.like(statusName, like, '\\'),
                cb.like(chefMatric, like, '\\'),
                cb.like(numeroStr, like, '\\'),
                cb.like(cb.lower(debStr), like, '\\'),
                cb.like(cb.lower(finStr), like, '\\')
            );
        };
    }


}
