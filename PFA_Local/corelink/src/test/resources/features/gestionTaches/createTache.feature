@creation_tache
Feature: Gestion des tâches — Création
  En tant que manager, je veux créer des tâches pour un projet de manière fiable,
  afin de piloter le projet en toute sécurité.

  Background: 
    Given je suis un manager
    When je valide "Pilotage des projets"
    And je valide "Liste"
    And je clique sur icon de gestion de taches pour le projet "Project Management"

  @TC-01 @creation_via_formulaire @Succès
  Scenario Outline: Création d’une Nouvelle tâche via le formulaire avec succès
    When j exécute la fonctionnalité "Nouvelle tâche"
    And je saisis les informations de la tâche:
      | titre    | <auto>                   |
      | priorité | Basse                    |
      | etat     | <etat>                   |
      | dateFin  | 20/10/2027 14:30         |
      | membres  | riahi salma, riahi sameh |
    And j exécute la fonctionnalité "Créer"
    Then un message "Succès" s’affiche avec le texte "La tâche a été créée avec succès !"
    And j exécute la fonctionnalité "Vue Tableau"
    Then je suis envoyé vers la liste des taches et je vois la tache crée avec:
      | titre      | <auto>                   |
      | flag-color | gris                     |
      | etat       | <etat>                   |
      | membres    | riahi salma, riahi sameh |
    And j exécute la fonctionnalité "Vue Kanban"
    And je filtre l'etat "<etat>" correspondant à la tâche crée
    Then je vois la même tâche affichée avec :
      | titre      | <auto>                   |
      | flag-color | gris                     |
      | etat       | <etat>                   |
      | membres    | riahi salma, riahi sameh |

    Examples: 
      | etat        |
      | A_FAIRE     |
      | EN_COURS    |
      | EN_REVISION |
      | TERMINE     |
      | BLOQUE      |
      | ANNULE      |

  @TC-02 @creation_via_kanban @Succès
  Scenario Outline: Création d’une Nouvelle tâche via la vue Kanban avec succès
    When je clique sur l’icône de création dans la colonne "<etat>"
    And je saisis les informations de la tâche:
      | titre    | <auto>                   |
      | priorité | Basse                    |
      | etat     | <etat>                   |
      | dateFin  | 20/10/2027 14:30         |
      | membres  | riahi salma, riahi sameh |
    And j exécute la fonctionnalité "Créer"
    Then un message "Succès" s’affiche avec le texte "La tâche a été créée avec succès !"
    And j exécute la fonctionnalité "Vue Tableau"
    Then je suis envoyé vers la liste des taches et je vois la tache crée avec:
      | titre      | <auto>                   |
      | flag-color | gris                     |
      | etat       | <etat>                   |
      | membres    | riahi salma, riahi sameh |
    And j exécute la fonctionnalité "Vue Kanban"
    And je filtre l'etat "<etat>" correspondant à la tâche crée
    Then je vois la même tâche affichée avec :
      | titre      | <auto>                   |
      | flag-color | gris                     |
      | etat       | <etat>                   |
      | membres    | riahi salma, riahi sameh |

    Examples: 
      | etat        |
      | A_FAIRE     |
      | EN_COURS    |
      | EN_REVISION |
      | TERMINE     |
      | BLOQUE      |
      | ANNULE      |

  @TC-03 @annulation_creation @neutre
  Scenario: Annulation de la création d’une tâche
    When j exécute la fonctionnalité "Nouvelle tâche"
    And j exécute la fonctionnalité "Annuler"
    Then je suis redirigé vers la vue Kanban des tâches dont la bouton "Vue Tableau" est visible
