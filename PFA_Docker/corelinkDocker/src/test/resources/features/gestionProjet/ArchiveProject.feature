@archive_projet
Feature: Pilotage des projets —  Archivage
  En tant que manager, je veux archiver des projets de façon fiable,
  afin de piloter mon portefeuille en sécurité et d’accorder l’accès selon les rôles.

  Background: 
    Given je suis identifié en tant que manager
    And je souhaite un projet auto crée
    And je le souhaite sélectionner en cliquant sur "archiver"
    And une pop up de confirmation s ouvre avec un titre "Archiver le projet ?" et un message "Voulez-vous archiver ce projet?"

  @TC-01-ArchiveProject-Confirm @functional @regression @archivage_confirmation
  Scenario: archiver un projet en succées
    And je valide l archivage
    Then Alerte de "Succès" avec un message "Le projet est archivé" sera visible
    And je souhaite chercher le projet
    Then un message d erreur s affiche " Aucun élément trouvé "

  @TC-02-ArchiveProject-Cancel @functional @archivage_annulation
  Scenario: annuler l archivage d un projet avec succees
    And j annule l archivage
    Then je souhaite voir que le projet persiste encore dans la liste
