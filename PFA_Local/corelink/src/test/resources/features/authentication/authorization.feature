@domain.authorization
Feature: Sécurité — Autorisation et Contrôle d’accès (CoreLink)
  En tant qu’utilisateur, je veux que l’accès aux pages soit contrôlé selon mon rôle,
  afin de garantir la confidentialité et la sécurité des espaces.

  @TC-01-Authorization-AccessProtectedPageWithoutAuth @p1 @error
  Scenario Outline: Accès direct à une page protégée sans authentification
    Given j ouvre le navigateur sur la page "url_CoreLink"
    And je navigue vers la page "<page>"
    Then je navigue vers la page "url_CoreLink"

    Examples: 
      | page        |
      | url_ADMIN   |
      | url_MANAGER |
      | url_MEMBER  |

  @TC-02-Authorization-RoleBasedControls @p1 @controle
  Scenario Outline: Contrôles d’autorisation par rôle
    Given je suis authentifié en tant que "<role>"
    When ce "<role>" accède à l URL "<url_refusee>"
    Then une SweetAlerte d erreur s affiche avec le titre "Accès refusé" et le message "Vous n’avez pas les droits nécessaires pour accéder à cette page."
    And je reste sur la page d’accueil du rôle "<role>"

    Examples: 
      | matricule         | mot_de_passe     | role    | url_refusee         |
      | admin.matricule   | admin.password   | ADMIN   | url_manager_refusee |
      | admin.matricule   | admin.password   | ADMIN   | url_member_refusee  |
      | manager.matricule | manager.password | MANAGER | url_admin_refusee   |
      | manager.matricule | manager.password | MANAGER | url_member_refusee  |
      | member.matricule  | member.password  | MEMBER  | url_admin_refusee   |
      | member.matricule  | member.password  | MEMBER  | url_manager_refusee |

  @TC-03-Authorization-UnknownURL-NonAuth @p2
  Scenario Outline: URL inconnue sans authentification (utilisateur non authentifié)
    Given j ouvre le navigateur sur la page "<page_depart>"
    When je navigue vers une URL inconnue "<url_inconnue_non_authentifie>"
    Then je suis redirigé vers la page "url_NotFound"
    And je clique sur le bouton pour revenir a ala page home
    Then je navigue vers la page "url_CoreLink"

    Examples: 
      | page_depart  | url_inconnue_non_authentifie |
      | url_CoreLink | url_inconnue1                |
      | url_CoreLink | url_inconnue2                |
      | url_CoreLink | url_inconnue3                |
      | url_login    | url_inconnue1                |
      | url_login    | url_inconnue2                |
      | url_login    | url_inconnue3                |

  @TC-04-Authorization-UnknownURL-Auth @p2 @userNONAUTH
  Scenario Outline: URL inconnue avec authentification (utilisateur authentifié)
    Given je suis authentifié en tant que "<role>"
    When je navigue vers une URL inconnue "<url_inconnue_authentifie>"
    Then je suis redirigé vers la page "url_NotFound"
    And je clique sur le bouton pour revenir a mon espace
    Then je suis redirigé vers la page d accueil du rôle "<role>"

    Examples: 
      | role    | url_inconnue_authentifie |
      | ADMIN   | url_inconnue4            |
      | MANAGER | url_inconnue5            |
      | MEMBER  | url_inconnue6            |
