@archivage_user
Feature: Gestion des comptes — Archivage d’utilisateurs (CoreLink)
  En tant qu’administrateur, je veux archiver ou annuler l’archivage d’un utilisateur actif,
  afin de maintenir la liste des comptes propre et sécurisée.

  Background: 
    Given je me connecte en tant qu’administrateur
    When un utilisateur actif auto-créé
    And je choisi de cliquer sur la bouton "Liste"
    And la page "Administration des utilisateurs" qui contenant la liste des utilisateurs de l application s’affiche
    And je sélectionne l’utilisateur puis je clique sur l’icône "supprimer"
    Then alerte pour confirmer la suppression avec le titre "Êtes-vous sûr ?" et le message "Supprimer l'utilisateur {matricule} ?" s’affiche

  @TC-01-ArchiveUser-Cancel @archivage_annulee
  Scenario: Annulation de l’archivage d’un utilisateur existant
    And j annule la suppression
    And je souhaite chercher l utilisateur dans la liste
    Then je vois l utilisateur dans la liste

  @TC-02-ArchiveUser-Confirm @archivage_confirmee
  Scenario: Archivage d’un utilisateur existant
    And je confirme la suppression
    Then une alerte succès avec le titre "Succès" et le message "Utilisateur supprimé avec succès" s’affiche
    And je souhaite chercher l utilisateur dans la liste
    Then un message de non confirmité est visible
