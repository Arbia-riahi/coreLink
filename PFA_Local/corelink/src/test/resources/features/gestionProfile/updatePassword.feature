@domain.utilisateurs @Modifier_mot_de_passe
Feature: Gestion du profil utilisateur — Modification des mot de passe
  En tant qu’utilisateur, je veux mettre à jour mon mot de passe afin de garder mon profil complet et sécurisé.

@TC-01-ProfileUpdate-PasswordSuccess @p1 @ui @functional @modification_password_nominal @succes
  Scenario Outline: modifier mot de passe en succées
    Given que j’ai ouvert une session en tant que "<role>"
    Then je souhaite consulter mon profile
    And je souhaite actionner sur "Mon Profile"
    And je souhaite actionner sur "Changer mot de passe"
    And je saisi l ancien mot de passe "<ancien password>"
    And je saisi le nouveau mot de passe "<nouveau mot de passe>"
    And je confirme le nouveau mot de passe "<confirm nouveau mot de passe>"
    Then je valide la modification avec " Mettre à jour "
    Then une alerte Succes dont le titre est "Succès" et le message est "Votre mot de passe a été modifié avec succès" s affiche
    And le localStorage sera vide
    And je serai envoyé vers la page de connexion pour se reconnecter "url_login"

    Examples: 
      | role    | ancien password        | nouveau mot de passe    | confirm nouveau mot de passe |
      | ADMIN   | admin.Ancienpassword   | admin.Nouveaupassword   | admin.Confirmpassword        |
      | MANAGER | manager.Ancienpassword | manager.Nouveaupassword | manager.Confirmpassword      |
      | MEMBER  | member.Ancienpassword  | member.Nouveaupassword  | member.Confirmpassword       |

  @TC-02-ProfileUpdate-PasswordFirstLogin @p1 @ui @functional @modification_password_first_Login @succes
  Scenario Outline: modifier mot de passe en succées lors de la 1 ere connexion
    Given je suis authentifiée tant que "<role>" pour la premiere fois
    Then je suis redigé vers "<url_update_password>" pour saisir un nouveau mot de passe
    And je saisi l ancien mot de passe "<ancien password>"
    And je saisi le nouveau mot de passe "<nouveau mot de passe>"
    And je confirme le nouveau mot de passe "<confirm nouveau mot de passe>"
    Then je valide la modification avec " Mettre à jour "
    Then une alerte Succes dont le titre est "Succès" et le message est "Votre mot de passe a été modifié avec succès" s affiche
    Then je suis redigé vers l espace "<espace>" reservé pour chaque utilisateur

    Examples: 
      | role    | url_update_password         | ancien password        | nouveau mot de passe    | confirm nouveau mot de passe | espace                |
      | MANAGER | url_update_password_manager | manager.Ancienpassword | manager.Nouveaupassword | manager.Confirmpassword      | Espace Chef de Projet |

  @TC-03-ProfileUpdate-PasswordErrorEmptyFields @p2 @ui @functional @modification_password @error @champs_vide
  Scenario Outline: Champ obligatoire lors de la modification
    Given que j’ai ouvert une session en tant que "<role>"
    Then je souhaite consulter mon profile
    And je souhaite actionner sur "Mon Profile"
    And je souhaite actionner sur "Changer mot de passe"
    And je saisi les informations de modifications:
      | ancien password              | <ancien password>              |
      | nouveau mot de passe         | <nouveau mot de passe>         |
      | confirm nouveau mot de passe | <confirm nouveau mot de passe> |
    Then je valide la modification avec " Mettre à jour "
    Then Error alerte avec "Champs manquants" et "Merci de fournir toutes les informations demandées" s affiche

    Examples: 
      | role    | ancien password        | nouveau mot de passe    | confirm nouveau mot de passe |
      | ADMIN   |                        | admin.Nouveaupassword   | admin.Confirmpassword        |
      | ADMIN   | admin.Ancienpassword   |                         | admin.Nouveaupassword        |
      | ADMIN   | admin.Ancienpassword   | admin.Nouveaupassword   |                              |
      | MANAGER |                        | manager.Nouveaupassword | manager.Confirmpassword      |
      | MANAGER | manager.Ancienpassword |                         | manager.Nouveaupassword      |
      | MANAGER | manager.Ancienpassword | manager.Nouveaupassword |                              |
      | MEMBER  |                        | member.Nouveaupassword  | member.Confirmpassword       |
      | MEMBER  | member.Ancienpassword  |                         | member.Nouveaupassword       |
      | MEMBER  | member.Ancienpassword  | member.Nouveaupassword  |                              |

  @TC-04-ProfileUpdate-PasswordErrorMismatch @p1 @ui @functional @modification_password @error @conformité_mdp
  Scenario Outline: le nouveau mot de passe  et le confirme mot de passes sont non conformes
    Given que j’ai ouvert une session en tant que "<role>"
    Then je souhaite consulter mon profile
    And je souhaite actionner sur "Mon Profile"
    And je souhaite actionner sur "Changer mot de passe"
    And je saisi les informations de modifications:
      | ancien password              | <ancien password>              |
      | nouveau mot de passe         | <nouveau mot de passe>         |
      | confirm nouveau mot de passe | <confirm nouveau mot de passe> |
    Then je valide la modification avec " Mettre à jour "
    Then Error alerte avec "Erreur" et "Les mots de passe ne correspondent pas !" s affiche

    Examples: 
      | role    | ancien password        | nouveau mot de passe   | confirm nouveau mot de passe |
      | ADMIN   | admin.Ancienpassword   | admin.Ancienpassword   | admin.Confirmpassword        |
      | MANAGER | manager.Ancienpassword | manager.Ancienpassword | manager.Nouveaupassword      |
      | MEMBER  | member.Ancienpassword  | member.Ancienpassword  | member.Nouveaupassword       |

  @TC-05-ProfileUpdate-PasswordErrorWrongOld @p1 @ui @functional @modification_password @error @incoreecte_ancien_mdp
  Scenario Outline: l'ancien mot de passes n'est pas correcte
    Given que j’ai ouvert une session en tant que "<role>"
    Then je souhaite consulter mon profile
    And je souhaite actionner sur "Mon Profile"
    And je souhaite actionner sur "Changer mot de passe"
    And je saisi un ancien mot de passe incorrecte "<ancien password>"
    And je saisi le nouveau mot de passe "<nouveau mot de passe>"
    And je confirme le nouveau mot de passe "<confirm nouveau mot de passe>"
    Then je valide la modification avec " Mettre à jour "
    Then Error alerte avec "Erreur" et "Ancien mot de passe incorrect !" s affiche

    Examples: 
      | role    | ancien password | nouveau mot de passe    | confirm nouveau mot de passe |
      | ADMIN   |             123 | admin.Nouveaupassword   | admin.Confirmpassword        |
      | MANAGER |             123 | manager.Nouveaupassword | manager.Confirmpassword      |
      | MEMBER  |             123 | member.Nouveaupassword  | member.Confirmpassword       |

  @TC-06-ProfileUpdate-PasswordErrorWeak @p1 @ui @functional @modification_password @error @faible_mdp
  Scenario Outline: le mot de passe est faible
    Given que j’ai ouvert une session en tant que "<role>"
    Then je souhaite consulter mon profile
    And je souhaite actionner sur "Mon Profile"
    And je souhaite actionner sur "Changer mot de passe"
    And je saisi les informations de modifications:
      | ancien password              | <ancien password>              |
      | nouveau mot de passe         | <nouveau mot de passe>         |
      | confirm nouveau mot de passe | <confirm nouveau mot de passe> |
    Then je valide la modification avec " Mettre à jour "
    Then Error alerte avec "Mot de passe faible" et "Le mot de passe doit contenir au moins 8 caractères, avec une majuscule, une minuscule, un chiffre et un caractère spécial." s affiche

    Examples: 
      | role    | ancien password         | nouveau mot de passe | confirm nouveau mot de passe |
      | ADMIN   | admin.Confirmpassword   |                  123 |                          123 |
      | MANAGER | manager.Nouveaupassword |                  123 |                          123 |
      | MEMBER  | member.Nouveaupassword  |                  123 |                          123 |