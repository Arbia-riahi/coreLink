package com.ProjectManagement.entities;

public enum TypeNotification {

    // === PROJET ===
    PROJET_DEADLINE_PROCHE,     // Deadline du projet approche (memeber + chef projet)
    PROJET_DEADLINE_DEPASSEE,   // Deadline du projet dépassée (memeber + chef projet)
    AFFECTATION_MEMBRE_PROJET,  // Membre ajouté au projet

    // === TÂCHE ===
    TACHE_ASSIGNATION,          // Tâche assignée à un membre (seulemenet memeber)
    TACHE_DEADLINE_PROCHE,      // Deadline de la tâche approche (seulemenet memeber)
    TACHE_DEADLINE_DEPASSEE,    // Deadline de la tâche dépassée (seulemenet memeber)

    // === COMMENTAIRES ===
    NOUVEAU_COMMENTAIRE,        // Nouveau commentaire ajouté (chef de projet + memebers)
    REPONSE_COMMENTAIRE,        // Réponse à un commentaire existant (pour le chef de projet + memebers)
    MENTION_DANS_COMMENTAIRE    // @mention dans un commentaire (pour le chef de projet + memebers)
}

  
