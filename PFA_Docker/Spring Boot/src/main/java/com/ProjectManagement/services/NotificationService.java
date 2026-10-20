package com.ProjectManagement.services;

import com.ProjectManagement.DTO.NotificationDTO;
import com.ProjectManagement.entities.*;
import com.ProjectManagement.repositories.NotificationRepository;
import com.ProjectManagement.websocket.NotificationWebSocketService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationWebSocketService webSocketService;

    public NotificationService(NotificationRepository notificationRepository,
                               NotificationWebSocketService webSocketService) {
        this.notificationRepository = notificationRepository;
        this.webSocketService = webSocketService;
    }

    // --- Crée et envoie une notification ---
    public NotificationDTO creerNotification(User destinataire, String message, TypeNotification type,
                                             Projet projet, Tache tache) {
        Notification notification = new Notification();
        notification.setDestinataire(destinataire);
        notification.setMessage(message);
        notification.setType(type);
        notification.setProjetLie(projet);
        notification.setTacheLiee(tache);
        notification.setDate(LocalDateTime.now());

        Notification saved = notificationRepository.save(notification);

        NotificationDTO dto = toDTO(saved);

        // Envoi en temps réel via WebSocket
        webSocketService.sendNotificationToUser(destinataire.getMatricule(), dto);

        return dto;
    }

    // --- Notifications pour tous les membres d'un projet ---
    public void creerNotificationPourMembresProjet(Projet projet, User auteur, String message, TypeNotification type) {
        for (User membre : projet.getMembres()) {
            if (!membre.equals(auteur)) {
                creerNotification(membre, message, type, projet, null);
            }
        }
    }

    // --- Notifications pour tous les membres d'une tâche ---
    public void creerNotificationPourMembresTache(Tache tache, User auteur, String message, TypeNotification type) {
        // Notifier l'assignee principal
        if (tache.getAssigneeA() != null && !tache.getAssigneeA().equals(auteur)) {
            creerNotification(tache.getAssigneeA(), message, type, tache.getProjet(), tache);
        }

        // Notifier les autres assignés
        if (tache.getAutresAssignes() != null) {
            for (User autre : tache.getAutresAssignes()) {
                if (!autre.equals(auteur)) {
                    creerNotification(autre, message, type, tache.getProjet(), tache);
                }
            }
        }
    }


    // --- Lister toutes les notifications pour un utilisateur ---
    public List<NotificationDTO> getNotificationsByUser(String matricule) {
        return notificationRepository.findByDestinataire_MatriculeOrderByDateDesc(matricule)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // --- Lister uniquement les notifications non lues ---
    public List<NotificationDTO> getUnreadNotifications(String matricule) {
        return notificationRepository.findByDestinataire_MatriculeAndLueFalseOrderByDateDesc(matricule)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // --- Marquer une notification comme lue ---
    public NotificationDTO markAsRead(Long id) {
        Notification notif = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification introuvable"));
        notif.setLue(true);
        return toDTO(notificationRepository.save(notif));
    }

    // --- Marquer toutes les notifications comme lues ---
    public void markAllAsRead(String matricule) {
        List<Notification> notifications = notificationRepository
                .findByDestinataire_MatriculeAndLueFalseOrderByDateDesc(matricule);
        for (Notification notif : notifications) {
            notif.setLue(true);
        }
        notificationRepository.saveAll(notifications);
    }

    // --- Supprimer une notification ---
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    // --- Conversion Notification -> DTO ---
    private NotificationDTO toDTO(Notification notif) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notif.getId());
        dto.setMessage(notif.getMessage());
        dto.setType(notif.getType().name());
        dto.setLue(notif.isLue());
        dto.setDate(notif.getDate());
        dto.setDestinataireMatricule(
            notif.getDestinataire() != null ? notif.getDestinataire().getMatricule() : null
        );
        dto.setTacheId(notif.getTacheLiee() != null ? notif.getTacheLiee().getId() : null);
        dto.setProjetId(notif.getProjetLie() != null ? notif.getProjetLie().getNumeroProjet() : null);
        return dto;
    }
    
    @Transactional
    public void notifierProjetDeadlineProche(Projet projet) {
        String message = "La deadline du projet \"" + projet.getTitre() + "\" approche.";
        // Notifier le chef de projet
        creerNotification(projet.getChefProjet(), message, TypeNotification.PROJET_DEADLINE_PROCHE, projet, null);
        // Notifier les membres
        creerNotificationPourMembresProjet(projet, projet.getChefProjet(), message, TypeNotification.PROJET_DEADLINE_PROCHE);
    }

    @Transactional
    public void notifierProjetDeadlineDepassee(Projet projet) {
        String message = "La deadline du projet \"" + projet.getTitre() + "\" est dépassée.";
        // Notifier le chef de projet
        creerNotification(projet.getChefProjet(), message, TypeNotification.PROJET_DEADLINE_DEPASSEE, projet, null);
        // Notifier les membres
        creerNotificationPourMembresProjet(projet, projet.getChefProjet(), message, TypeNotification.PROJET_DEADLINE_DEPASSEE);
    }

    @Transactional
    public void notifierTacheDeadlineProche(Tache tache) {
        String message = "La deadline de la tâche \"" + tache.getTitre() + "\" approche.";
        // Notifier les assignés (principal + autres)
        if (tache.getAssigneeA() != null) {
            creerNotification(tache.getAssigneeA(), message, TypeNotification.TACHE_DEADLINE_PROCHE, tache.getProjet(), tache);
        }
        if (tache.getAutresAssignes() != null) {
            for (User membre : tache.getAutresAssignes()) {
                creerNotification(membre, message, TypeNotification.TACHE_DEADLINE_PROCHE, tache.getProjet(), tache);
            }
        }
    }

    @Transactional
    public void notifierTacheDeadlineDepassee(Tache tache) {
        String message = "La deadline de la tâche \"" + tache.getTitre() + "\" est dépassée.";
        // Notifier les assignés
        if (tache.getAssigneeA() != null) {
            creerNotification(tache.getAssigneeA(), message, TypeNotification.TACHE_DEADLINE_DEPASSEE, tache.getProjet(), tache);
        }
        if (tache.getAutresAssignes() != null) {
            for (User membre : tache.getAutresAssignes()) {
                creerNotification(membre, message, TypeNotification.TACHE_DEADLINE_DEPASSEE, tache.getProjet(), tache);
            }
        }
    }

    // --- Notifications liées aux commentaires ---
    @Transactional
    public void notifierNouveauCommentaire(User auteur, Tache tache, String contenu) {
        String message = auteur.getNom() + " " + auteur.getPrenom() + " a commenté la tâche \"" + tache.getTitre() + "\" : " + contenu;

        // ✅ Notifier le chef du projet
        User chef = tache.getProjet().getChefProjet();
        if (!chef.equals(auteur)) {
            creerNotification(chef, message, TypeNotification.NOUVEAU_COMMENTAIRE, tache.getProjet(), tache);
        }

        // ✅ Notifier l'assignee principal
        if (tache.getAssigneeA() != null && !tache.getAssigneeA().equals(auteur)) {
            creerNotification(tache.getAssigneeA(), message, TypeNotification.NOUVEAU_COMMENTAIRE, tache.getProjet(), tache);
        }

        // ✅ Notifier les autres assignés
        if (tache.getAutresAssignes() != null) {
            for (User membre : tache.getAutresAssignes()) {
                if (!membre.equals(auteur)) {
                    creerNotification(membre, message, TypeNotification.NOUVEAU_COMMENTAIRE, tache.getProjet(), tache);
                }
            }
        }
    }


    @Transactional
    public void notifierReponseCommentaire(User auteur, Projet projet, String contenu) {
        String message = auteur.getNom() + " " + auteur.getPrenom() + " a répondu à un commentaire : " + contenu;
        creerNotification(projet.getChefProjet(), message, TypeNotification.REPONSE_COMMENTAIRE, projet, null);
        creerNotificationPourMembresProjet(projet, auteur, message, TypeNotification.REPONSE_COMMENTAIRE);
    }

    @Transactional
    public void notifierMentionDansCommentaire(User auteur, Projet projet, User mentionne) {
        String message = auteur.getNom() + " vous a mentionné dans un commentaire sur le projet : " + projet.getTitre();
        creerNotification(mentionne, message, TypeNotification.MENTION_DANS_COMMENTAIRE, projet, null);
    }


    public long countUnreadNotifications(String matricule) {
        return notificationRepository.countByDestinataireMatriculeAndLueFalse(matricule);
    }

}
