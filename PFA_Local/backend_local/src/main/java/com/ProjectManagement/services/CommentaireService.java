package com.ProjectManagement.services;

import com.ProjectManagement.DTO.CommentaireDTO;
import com.ProjectManagement.entities.*;
import com.ProjectManagement.repositories.CommentaireRepository;
import com.ProjectManagement.repositories.TacheRepository;
import com.ProjectManagement.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CommentaireService {
	 private final CommentaireRepository commentaireRepository;
	    private final TacheRepository tacheRepository;
	    private final UserRepository userRepository;
	    private final NotificationService notificationService;

	    public CommentaireService(CommentaireRepository commentaireRepository,
	                              TacheRepository tacheRepository,
	                              UserRepository userRepository,
	                              NotificationService notificationService) {
	        this.commentaireRepository = commentaireRepository;
	        this.tacheRepository = tacheRepository;
	        this.userRepository = userRepository;
	        this.notificationService = notificationService;
	    }

    // URL par défaut si l'utilisateur n'a pas d'image
    private static final String DEFAULT_IMAGE_URL = "http://localhost:8080/uploads/files/default-avatar.png";

  

    /**
     * Récupérer tous les commentaires d'une tâche
     */
    public List<CommentaireDTO> getCommentairesDTOByTache(Long tacheId) {
        Tache tache = tacheRepository.findById(tacheId)
                .orElseThrow(() -> new EntityNotFoundException("Tâche non trouvée : " + tacheId));

        return commentaireRepository.findByTacheOrderByDateAsc(tache)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    /**
     * Ajouter un commentaire ou une réponse
     */
    public CommentaireDTO ajouterCommentaire(Long tacheId, String matriculeAuteur, String contenu, Long parentCommentId) {
        Tache tache = tacheRepository.findById(tacheId)
                .orElseThrow(() -> new EntityNotFoundException("Tâche non trouvée : " + tacheId));
        User auteur = userRepository.findById(matriculeAuteur)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé : " + matriculeAuteur));

        Commentaire commentaire = new Commentaire();
        commentaire.setContenu(contenu.trim());
        commentaire.setDate(LocalDateTime.now());
        commentaire.setAuteur(auteur);
        commentaire.setTache(tache);

        // Notification pour le parent
        if (parentCommentId != null) {
            Commentaire parent = commentaireRepository.findById(parentCommentId)
                    .orElseThrow(() -> new EntityNotFoundException("Commentaire parent non trouvé : " + parentCommentId));
            commentaire.setParentComment(parent);

            if (!parent.getAuteur().equals(auteur)) {
                notificationService.creerNotification(
                        parent.getAuteur(),
                        auteur.getNom() + " a répondu à votre commentaire : " + contenu,
                        TypeNotification.REPONSE_COMMENTAIRE,
                        tache.getProjet(),
                        tache
                );
            }
        }

        // Mentions
        List<User> mentions = detecterMentions(contenu);
        for (User u : mentions) {
            if (!u.equals(auteur)) {
                notificationService.creerNotification(
                        u,
                        auteur.getNom() + " vous a mentionné dans un commentaire : " + contenu,
                        TypeNotification.MENTION_DANS_COMMENTAIRE,
                        tache.getProjet(),
                        tache
                );
            }
        }

        Commentaire saved = commentaireRepository.save(commentaire);

        // Notification générique aux membres
     // ✅ Nouvelle méthode complète
        notificationService.notifierNouveauCommentaire(auteur, tache, contenu);


        return convertToDTO(saved);
    }

    /**
     * Modifier un commentaire
     */
    public CommentaireDTO modifierCommentaire(Long commentaireId, String nouveauContenu) {
        if (nouveauContenu == null || nouveauContenu.trim().isEmpty()) {
            throw new IllegalArgumentException("Le contenu du commentaire ne peut pas être vide.");
        }

        Commentaire commentaire = commentaireRepository.findById(commentaireId)
                .orElseThrow(() -> new EntityNotFoundException("Commentaire non trouvé : " + commentaireId));
        commentaire.setContenu(nouveauContenu.trim());
        commentaire.setModifie(true);
        Commentaire updated = commentaireRepository.save(commentaire);

        return convertToDTO(updated);
    }

    /**
     * Supprimer un commentaire
     */
    public void supprimerCommentaire(Long commentaireId) {
        Commentaire commentaire = commentaireRepository.findById(commentaireId)
                .orElseThrow(() -> new EntityNotFoundException("Commentaire non trouvé : " + commentaireId));
        Tache tache = commentaire.getTache();
        commentaireRepository.delete(commentaire);

        tache.setNombreCommentaires(Math.max(0, tache.getNombreCommentaires() - 1));
        tacheRepository.save(tache);
    }

    /**
     * Convertir un Commentaire en DTO
     */
    private CommentaireDTO convertToDTO(Commentaire c) {
        String imageUrl = (c.getAuteur() != null && c.getAuteur().getImage() != null)
                ? "http://localhost:8080/uploads/files/" + c.getAuteur().getImage()
                : DEFAULT_IMAGE_URL;

        return new CommentaireDTO(
                c.getId(),
                c.getContenu(),
                c.getDate(),
                c.getAuteur() != null ? c.getAuteur().getNom() : "Inconnu",
                c.getAuteur() != null ? c.getAuteur().getPrenom() : "Inconnu",
                imageUrl
        );
    }

    /**
     * Détection des mentions
     */
    private List<User> detecterMentions(String contenu) {
        List<User> mentions = new ArrayList<>();
        String regex = "@([\\p{L}]+)\\s+([\\p{L}]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(contenu);

        while (matcher.find()) {
            String nom = matcher.group(1);
            String prenom = matcher.group(2);
            userRepository.findByNomAndPrenom(nom, prenom).ifPresent(u -> {
                if (!mentions.contains(u)) mentions.add(u);
            });
        }
        return mentions;
    }
}
