@create_projet
Feature: Pilotage des projets — Création
  En tant que manager, je veux créer des projets de façon fiable,
  afin de piloter mon portefeuille en sécurité et d’accorder l’accès selon les rôles.

  Background: 
    Given je suis connecté en tant que manager
    When j appuie sur la touche "Pilotage des projets"
    And j appuie sur la touche "Ajouter"

  @TC-01-CreateProject @p1 @ui @functional @smoke @creation_success
  Scenario: Création d un nouveau projet avec succès
    Then un formulaire vierge est bien visible sur la page de création
    And je saisis les informations relatives a un projet :
      | titre       | <auto>                                                           |
      | description | <auto>                                                           |
      | dateDebut   | <auto>                                                           |
      | dateFin     | <auto>                                                           |
      | statut      | En cours                                                         |
      | membres     | riahi salma, mtiri chiraz, mtiri fahed, riahi pouka, riahi sameh |
      | photos      | image 1.jpeg, BDD.pdf, cas de tests.xlsx                         |
    And j appuie sur la touche " Créer Projet "
    Then Alerte de "Succès" avec un message "Projet créé avec succès" sera visible
    And je suis envoyé vers la liste des projets et je vois un projet avec :
      | titre     | <auto>   |
      | dateDebut | <auto>   |
      | dateFin   | <auto>   |
      | statut    | En Cours |
    And je clique sur icon "details" pour voir tous les informations de projet :
      | titre       | <auto>                                                           |
      | description | <auto>                                                           |
      | dateDebut   | <auto>                                                           |
      | dateFin     | <auto>                                                           |
      | statut      | En cours                                                         |
      | membres     | riahi salma, mtiri chiraz, mtiri fahed, riahi pouka, riahi sameh |
      | photos      | image 1.jpeg, BDD.pdf, cas de tests.xlsx                         |

  @TC-02-CreateProject-InvalidStartDate @p1 @ui @functional @negative @validation @creation_error_dateDebut
  Scenario Outline: Rejeter une date de début antérieure à aujourd’hui
    Then un formulaire vierge est bien visible sur la page de création
    And je saisis une date de debut antierieure a aujourd'hui "<dateDebut>"
    Then un message d’erreur concernat la date de debut sera affiché au dessous "<message>"

    Examples: 
      | dateDebut | message                                              |
      | yesterday | La date de début doit être aujourd’hui ou plus tard. |
      | today-1d  | La date de début doit être aujourd’hui ou plus tard. |
      | today-7d  | La date de début doit être aujourd’hui ou plus tard. |
      | lastWeek  | La date de début doit être aujourd’hui ou plus tard. |
      | lastYear  | La date de début doit être aujourd’hui ou plus tard. |

  @TC-03-CreateProject-InvalidEndDate @p1 @ui @functional @negative @validation @creation_error_dateFin
  Scenario Outline: Rejeter une date de fin antérieure à date de debut
    Then un formulaire vierge est bien visible sur la page de création
    And je saisis une date de debut valide "<dateDebut>"
    And je saisis une date de fin antierieure a date debut "<dateFin>"
    Then un message d’erreur concernat la date de fin sera affiché au dessous "<message>"

    Examples: 
      | dateDebut | dateFin  | message                                                           |
      | today     | today-1d | La date de fin doit être postérieure ou égale à la date de début. |
      | today     | today-20 | La date de fin doit être postérieure ou égale à la date de début. |
      | today     | today-7d | La date de fin doit être postérieure ou égale à la date de début. |

  @TC-04-CreateProject-RequiredFields @p1 @ui @functional @negative @validation @smoke @creation_error
  Scenario Outline: champs obligatoires lors de la creation
    Then un formulaire vierge est bien visible sur la page de création
    And je saisis les informations relatives a un projet :
      | titre       | <titre>                                  |
      | description | <description>                            |
      | dateDebut   | <dateDebut>                              |
      | dateFin     | <dateFin>                                |
      | statut      | <statut>                                 |
      | membres     | <membres>                                |
      | photos      | image 1.jpeg, BDD.pdf, cas de tests.xlsx |
    Then la bouton " Créer Projet " est non cliquable

    Examples: 
      | titre   | description      | dateDebut | dateFin | statut   | membres                                                          |
      |         | bonjour mes amis | today     | today+7 | En cours | riahi salma, mtiri chiraz, mtiri fahed, riahi pouka, riahi sameh |
      | projet5 |                  | today     | today+7 | En cours | riahi salma, mtiri chiraz, mtiri fahed, riahi pouka, riahi sameh |
      | projet5 | bonjour mes amis |           | today+7 | En cours | riahi salma, mtiri chiraz, mtiri fahed, riahi pouka, riahi sameh |
      | projet5 | bonjour mes amis | today     |         | En cours | riahi salma, mtiri chiraz, mtiri fahed, riahi pouka, riahi sameh |
      | projet5 | bonjour mes amis | today     | today+7 |          | riahi salma, mtiri chiraz, mtiri fahed, riahi pouka, riahi sameh |
      | projet5 | bonjour mes amis | today     | today+7 | En cours |                                                                  |
