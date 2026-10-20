@domain.logout
Feature: Sécurité — Déconnexion des utilisateurs (CoreLink)
  En tant qu’utilisateur, je veux pouvoir me déconnecter correctement,
  afin de sécuriser ma session et libérer mes accès.

@TC-01-Logout-Success @p1 @ui @functional @smoke
Scenario Outline: Déconnexion valide
  Given je suis authentifié en tant que un utilisateur avec le role "<role>"
  When j ouvre le menu utilisateur dans l’en-tête et le menu s affiche
  And je clique sur le bouton de deconnexion
  Then je suis renvoyé vers la page "url_login"
  And je re accede vers les espaces reserves aux utilsateurs d application "<espace>"
  Then je suis redirigé vers la page home "url_CoreLink"
  And le localStorage ne contient plus le jeton d’authentification

  Examples: 
    | role    | espace               |
    | ADMIN   | espaceAdministrateur |
    | MANAGER | espaceManager        |
    | MEMBER  | espaceMember         |
