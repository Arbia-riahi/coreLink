@update_projet
Feature: Pilotage des projets —  Modification
  En tant que manager, je veux mettre à jour des projets de façon fiable,
  afin de piloter mon portefeuille en sécurité et d’accorder l’accès selon les rôles.

  Background: 
    Given je souhaite accéder a la plateforme en tant que manager
    And un projet est auto crée
    And je sélectione un et je clique sur icon "modifier"
    Then un formulaire déja pré-rempli sera ouvert

  @TC-01-UpdateProject-FullSuccess @functional @regression @modification_complete_succes
  Scenario Outline: modifier un projet avec sucées
    And je souhaite modifier tous les champs :
      | titre       | <auto>                                    |
      | description | <auto>                                   |
      | dateDebut   | <auto>                                   |
      | dateFin     | <auto>                                   |
      | statut      | En Attente                               |
      | membres     | riahi salma, mtiri chiraz, mtiri fahed   |
      | images    | image 1.jpeg, BDD.pdf, cas de tests.xlsx |
    And je souhaite cliquer sur "Modifier Projet"
    Then pop-up de "Succès" avec un message "Projet modifié avec succès" sera visible
    And je suis réenvoyé vers la liste des projets et je souhaite voir un projet avec :
      | titre     | <auto>     |
      | dateDebut | <auto>     |
      | dateFin   | <auto>     |
      | statut    | En Attente |
    And je clique sur "details" pour voir tous les informations de projet :
      | titre       | <auto>                                    |
      | description | <auto>                                   |
      | dateDebut   | <auto>                                   |
      | dateFin     | <auto>                                   |
      | statut      | En Attente                               |
      | membres     | riahi salma, mtiri chiraz, mtiri fahed   |
      | images    | image 1.jpeg, BDD.pdf, cas de tests.xlsx |

  @TC-02-UpdateProject-PartialSuccess @p2 @functional @modification_partielle_succes
  Scenario Outline: modifier un projet avec sucées
    And je souhaite modifier tous les champs :
      | titre  | <auto> |
      | statut | Annulé |
    And je souhaite cliquer sur "Modifier Projet"
    Then pop-up de "Succès" avec un message "Projet modifié avec succès" sera visible
    And je suis dirigé vers la liste des projets et je souhaite voir:
      | titre     | <auto> |
      | dateDebut | <auto> |
      | dateFin   | <auto> |
      | statut    | Annulé |
    And je clique sur "details" pour voir tous les informations de projet :
      | titre     | <auto> |
      | dateDebut | <auto> |
      | dateFin   | <auto> |
      | statut    | Annulé |

  @TC-03-UpdateProject-NoChangeDisabled @p2 @ui @functional @negative @validation @modification_sans_changement
  Scenario Outline: modifier un projet avec sucées
    And la bouton "Modifier Projet" est non cliquable

  @TC-04-UpdateProject-InvalidStartDate @p1 @ui @functional @negative @validation @modification_error_date_debut
  Scenario Outline: Rejeter une date de début antérieure à aujourd’hui
    And je souhaite saisir une date de debut antierieure a aujourd'hui "<dateDebut>"
    Then un message d’erreur s'affiche "<message>" pour la date de debut

    Examples: 
      | dateDebut | message                                                  |
      | yesterday | La date de début doit être aujourd'hui ou dans le futur. |
      | today-1d  | La date de début doit être aujourd'hui ou dans le futur. |
      | today-7d  | La date de début doit être aujourd'hui ou dans le futur. |
      | lastWeek  | La date de début doit être aujourd'hui ou dans le futur. |
      | lastYear  | La date de début doit être aujourd'hui ou dans le futur. |

  @TC-05-UpdateProject-InvalidEndDate @p1 @ui @functional @negative @validation @modification_error_date_fin
  Scenario Outline: Rejeter une date de fin antérieure à date de debut
    And je souhaite saisir une date de fin antierieure a date debut "<dateFin>"
    Then un message d’erreur s'affiche "<message>" pour la date de fin

    Examples: 
      | dateDebut | dateFin  | message                                                  |
      | today     | today-1d | La date de fin doit être postérieure à la date de début. |
      | today     | today-20 | La date de fin doit être postérieure à la date de début. |
      | today     | today-7d | La date de fin doit être postérieure à la date de début. |
