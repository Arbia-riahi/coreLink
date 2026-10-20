@serachAndPaginationUser
Feature: Gestion des comptes — Recherche, Filtres et Pagination (CoreLink)
  En tant qu’administrateur, je veux filtrer et paginer la liste des utilisateurs,
  afin de naviguer efficacement et trouver les comptes souhaités.

  Background: 
    Given je login en tant que administrateur
    And je valide la bouton "Gérer les comptes"
    And je valide la bouton "Liste"
    Then je vois la liste des utilisateurs "Administration des utilisateurs"

  @TC-01-Search-EmptyPage
  Scenario Outline: Page vide après filtre
    When j applique le filtre "<query>"
    Then un message "Aucun résultat à afficher pour « <query> »." est visible
    And les contrôles de pagination sont désactivés

    Examples: 
      | query          |
      | no-result-xyz  |
      | not@exists.tld |
      | %%%%%%         |

  @TC-02-Search-ResultsPresent
  Scenario Outline: Filtrer avec résultats (expressions supportées)
    When j applique le filtre "<expr>"
    Then tous les résultats "<result>" qui corresponds a ce filtre "<expr>" s affichent dans le tableau

    Examples: 
      | expr          | result        |
      | MANAGER       | MANAGER       |
      | member        | MEMBER        |
      | ala           | ala           |
      | MANAGER       | MANAGER       |
      | ala@gmail.com | ala@gmail.com |

  @TC-03-Pagination-RowsPerPage
  Scenario: Nombre de lignes par page (4 par page)
    Then je vois exactement 4 lignes dans le tableau
    And le bouton "Précédent" est non cliquable
    And le bouton "Suivant" est cliquable

  @TC-04-Pagination-NextPrev
  Scenario: Navigation Suivant / Précédent
    And je mémorise le matricule de la 1ère ligne comme "firstPageRow1"
    And je valide la bouton "Suivant"
    Then le matricule de la 1 ère ligne est différent de "firstPageRow1"
    And je valide la bouton "Précédent"
    Then le matricule de la 1 ère ligne est égal à "firstPageRow1"

  @TC-05-Pagination-FirstLastDisabled
  Scenario: Désactivation en première et dernière page
    And je vais à la première page
    Then le bouton "Précédent" est non cliquable
    And le bouton "Suivant" est cliquable
    When je vais à la dernière page
    Then le bouton "Suivant" est non cliquable
    And le bouton "Précédent" est cliquable
