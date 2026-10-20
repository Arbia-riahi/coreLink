package com.ProjectManagement.services;

import com.ProjectManagement.entities.*;
import com.ProjectManagement.repositories.ProjetRepository;
import com.ProjectManagement.repositories.TacheRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;

@Service
public class DeadlineNotificationService {

    private final ProjetRepository projetRepository;
    private final TacheRepository tacheRepository;
    private final NotificationService notificationService;

    public DeadlineNotificationService(ProjetRepository projetRepository,
                                       TacheRepository tacheRepository,
                                       NotificationService notificationService) {
        this.projetRepository = projetRepository;
        this.tacheRepository = tacheRepository;
        this.notificationService = notificationService;
    }

   

     

      /**
       * Toutes les 45 minutes.
       * - Projet:
       *    - proche: dateFin == demain
       *    - dépassée: dateFin < aujourd’hui
       * - Tâche (LocalDateTime):
       *    - proche: 0 < dateFin - maintenant ≤ 24h
       *    - dépassée: dateFin < maintenant
       */
      @Scheduled(cron = "0 */45 * * * *")
      @Transactional
      public void checkDeadlines() {
        final ZoneId zone = ZoneId.systemDefault();
        final LocalDate today = LocalDate.now(zone);
        final LocalDate tomorrow = today.plusDays(1);
        final LocalDateTime now = LocalDateTime.now(zone);
        final LocalDateTime in24h = now.plusHours(24);

        // --- PROJETS (date au jour)
        for (Projet p : projetRepository.findAllWithMembresEtChef()) {
          if (p.getDateFin() == null || p.isArchived()) continue;

          // proche (demain)
          if (p.getDateFin().isEqual(tomorrow) && !p.isDeadlineProcheNotifiee()) {
            envoyerNotifProjet(
              p,
              "La deadline du projet '" + p.getTitre() + "' est proche (24h restantes).",
              TypeNotification.PROJET_DEADLINE_PROCHE
            );
            p.setDeadlineProcheNotifiee(true);
            projetRepository.save(p);
          }

          // dépassée (jour strictement avant aujourd’hui)
          if (p.getDateFin().isBefore(today) && !p.isDeadlineDepasseeNotifiee()) {
            envoyerNotifProjet(
              p,
              "La deadline du projet '" + p.getTitre() + "' est dépassée !",
              TypeNotification.PROJET_DEADLINE_DEPASSEE
            );
            p.setDeadlineDepasseeNotifiee(true);
            projetRepository.save(p);
          }
        }

        // --- TACHES (précision à l’heure)
        for (Tache t : tacheRepository.findAll()) {
          if (t.getDateFin() == null || t.isArchived()) continue;

          // proche: dans les 24h (strictement après maintenant)
          if (!t.isDeadlineProcheNotifiee()
              && t.getDateFin().isAfter(now)
              && ( !t.getDateFin().isAfter(in24h) )) {
            envoyerNotifTache(
              t,
              "La deadline de la tâche '" + t.getTitre() + "' approche (<= 24h).",

              TypeNotification.TACHE_DEADLINE_PROCHE
            );
            t.setDeadlineProcheNotifiee(true);
            tacheRepository.save(t);
          }

          // dépassée: avant maintenant
          if (!t.isDeadlineDepasseeNotifiee()
              && t.getDateFin().isBefore(now)) {
            envoyerNotifTache(
              t,
              "La deadline de la tâche '" + t.getTitre() + "' est dépassée !",
              TypeNotification.TACHE_DEADLINE_DEPASSEE
            );
            t.setDeadlineDepasseeNotifiee(true);
            tacheRepository.save(t);
          }
        }
      }

      // 🔹 Projet : chef + membres
      private void envoyerNotifProjet(Projet projet, String message, TypeNotification type) {
        notificationService.creerNotification(projet.getChefProjet(), message, type, projet, null);
        System.out.println("[NOTIF DEADLINE] Chef : " + projet.getChefProjet().getMatricule());
        for (User membre : projet.getMembres()) {
        	 System.out.println("[NOTIF DEADLINE] Membre : " + membre.getMatricule());
          if (!membre.equals(projet.getChefProjet())) {
            notificationService.creerNotification(membre, message, type, projet, null);
          }
        }
      }
      
      
   


      // 🔹 Tâche : assignee principal + autres assignés (pas le chef si non assigné)
      private void envoyerNotifTache(Tache tache, String message, TypeNotification type) {
        if (tache.getAssigneeA() != null) {
          notificationService.creerNotification(tache.getAssigneeA(), message, type, tache.getProjet(), tache);
        }
        if (tache.getAutresAssignes() != null) {
          for (User membre : tache.getAutresAssignes()) {
            notificationService.creerNotification(membre, message, type, tache.getProjet(), tache);
          }
        }
      }
    

    // --- Notifications liées aux commentaires ---
    public void notifierNouveauCommentaire(Projet projet, User auteur, String messageCommentaire) {
        // Notifie chef + membres
        envoyerNotifProjet(
                projet,
                "Nouveau commentaire dans le projet '" + projet.getTitre() + "' : " + messageCommentaire,
                TypeNotification.NOUVEAU_COMMENTAIRE
        );
    }

    public void notifierReponseCommentaire(Projet projet, User auteur, String messageReponse) {
        // Notifie chef + membres
        envoyerNotifProjet(
                projet,
                "Réponse à un commentaire dans le projet '" + projet.getTitre() + "' : " + messageReponse,
                TypeNotification.REPONSE_COMMENTAIRE
        );
    }

    public void notifierMentionDansCommentaire(Projet projet, User mentionne, String messageCommentaire) {
        // Notifie seulement la personne mentionnée
        notificationService.creerNotification(
                mentionne,
                "Vous avez été mentionné dans un commentaire du projet '" + projet.getTitre() + "' : " + messageCommentaire,
                TypeNotification.MENTION_DANS_COMMENTAIRE,
                projet,
                null
        );
    }
}





  

    
    

