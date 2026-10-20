@domain.login
Feature: Sécurité — Authentification
  En tant qu’utilisateur,
  je veux pouvoir me connecter à la plateforme ,
  afin d’accéder à mon espace personnel et aux fonctionnalités qui me sont réservées.

  @TC-01-Login-SuccessRoles
  Scenario Outline: Connexion réussie avec des identifiants valides selon le rôle de l’utilisateur
    Given j ouvre le navigateur sur "url_CoreLink"
    And je clique sur le bouton "Connexion"
    Then je suis redirigé vers la page de connexion "url_login"
    And je saisis le matricule "<matricule>"
    And je saisis le mot de passe "<mot_de_passe>"
    And je clique sur le bouton "Se connecter"
    Then une alerte de succès s affiche avec le titre "Connexion réussie" et le message "Vous êtes connecté en tant que <role>"
    And je suis redirigé vers la page d accueil du rôle "<role>"
    And l’espace affiché pour l’utilisateur authentifié est "<espace>"

    Examples: 
      | role    | matricule         | mot_de_passe     | espace                |
      | ADMIN   | admin.matricule   | admin.password   | Espace Administrateur |
      | MANAGER | manager.matricule | manager.password | Espace Chef de Projet |
      | MEMBER  | member.matricule  | member.password  | Espace collaborateurs |

  @TC-02-visualisation_mot_de_passe
  Scenario Outline: Afficher et masquer le mot de passe sur la page de connexion
    Given j ouvre le navigateur sur "url_CoreLink"
    And je clique sur le bouton "Connexion"
    Then je suis redirigé vers la page de connexion "url_login"
    And je saisis le mot de passe "<mot_de_passe>"
    And Je clique sur l'icône œil pour visualiser le mot de passe
    Then Le "<mot_de_passe>" est affiché en clair dans le champ
    When Je clique à nouveau sur l'icône œil
    Then Le mot de passe est à nouveau masqué

    Examples: 
      | mot_de_passe     |
      | admin.password   |
      | manager.password |
      | member.password  |

  @TC-03-Login-Errors @negative @error
  Scenario Outline: Connexion échouée — cas d’erreur variés
    Given j ouvre le navigateur sur "url_CoreLink"
    And je clique sur le bouton "Connexion"
    Then je suis redirigé vers la page de connexion "url_login"
    And je saisis le matricule "<matricule>"
    And je saisis le mot de passe "<mot_de_passe>"
    And je clique sur le bouton "Se connecter"
    Then une alerte d erreur s affiche avec le titre "<titre>" et le message "<message>"

    Examples: 
      | matricule | mot_de_passe | titre                   | message                                                |
      | aouou     | Ala@123*     | Identifiants incorrects | Matricule ou mot de passe incorrect.                   |
      | ala       | Ala@123455*  | Identifiants incorrects | Matricule ou mot de passe incorrect.                   |
      | houma123  | tek123*87    | Identifiants incorrects | Matricule ou mot de passe incorrect.                   |
      |           | Tekup123*    | Champs manquants        | Veuillez saisir votre matricule et votre mot de passe. |
      | ala       |              | Champs manquants        | Veuillez saisir votre matricule et votre mot de passe. |
      |           |              | Champs manquants        | Veuillez saisir votre matricule et votre mot de passe. |
     #| arbia     | Ala@123*     | Serveur indisponible    | Impossible de se connecter au serveur. Veuillez réessayer plus tard. |
