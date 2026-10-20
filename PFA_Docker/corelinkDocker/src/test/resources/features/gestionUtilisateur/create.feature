@create_user
Feature: Gestion des comptes — Création d’utilisateurs (CoreLink)
  En tant qu’administrateur, je veux créer de nouveaux utilisateurs de façon sécurisée et conforme,
  afin d’accorder des accès au bon rôle et garantir l’intégrité des comptes.

  Background: 
    Given je me connecte en tant que admin
    And j appui sur "Gérer les comptes"

  @TC-01-CreateUser-Success @p1 @ui @functional @smoke @creation_success
  Scenario: Création d un nouvel utilisateur avec succès
    When j appui sur "Ajouter"
    And le formulaire de création s ouvre avec le titre "Créer un nouvel utilisateur"
    And je saisis les informations d utilisateur :
      | matricule | <auto>       |
      | nom       | <auto>       |
      | prenom    | <auto>       |
      | email     | <auto>       |
      | password  | <auto>       |
      | role      | MANAGER      |
      | image     | image 1.jpeg |
    And j appui sur "Créer Utilisateur"
    Then SweetAlert de succès avec le titre "Succès" et le message "Utilisateur créé avec succès" s affiche sur la page de création de l utilisateur
    And je suis envoyé vers la liste des utilisateurs et je vois utilisateur avec :
      | matricule | <auto>       |
      | nom       | <auto>       |
      | prenom    | <auto>       |
      | email     | <auto>       |
      | role      | MANAGER      |
      | image     | image 1.jpeg |

  @TC-02-CreateUser-ErrorFormat @p1 @ui @functional @negative @validation @creation @error_format
  Scenario Outline: Erreurs de format lors de la création de l utilisateur
    When j appui sur "Ajouter"
    And le formulaire de création s ouvre avec le titre "Créer un nouvel utilisateur"
    And je saisis les informations d utilisateur :
      | matricule | <matricule> |
      | nom       | <nom>       |
      | prenom    | <prenom>    |
      | email     | <email>     |
      | password  | <password>  |
      | role      | <role>      |
      | image     | <image>     |
    And j appui sur "Créer Utilisateur"
    Then SweetAlert d erreur avec le titre "<titre>" et le message "<message>" s affiche sur la page de création de l utilisateur

    Examples: 
      # Email invalide -> seul "email" est volontairement faux, le reste en <auto> pour éviter les collisions
      | matricule | nom    | prenom | email    | password | role    | image             | titre  | message                                                                                                               |
      | <auto>    | <auto> | <auto> | test.com | John123* | MANAGER | image 1.jpeg      | Erreur | Veuillez entrer un email valide                                                                                       |
      # Password invalide -> seul "password" est faux, le reste en <auto>
      | <auto>    | <auto> | <auto> | <auto>   |      123 | MEMBER  | image 1.jpeg      | Erreur | Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial |
      # Image invalide -> seul "image" est invalide, le reste en <auto>
      | <auto>    | <auto> | <auto> | <auto>   | Ala@123* | MEMBER  | cas de tests.xlsx | Erreur | Veuillez sélectionner une image valide (jpg, png, etc.)                                                               |

  @TC-03-CreateUser-ErrorDuplicate @p1 @negative @validation @error_doublon
  Scenario Outline: Erreur de duplication de matricule ou d email
    When j appui sur "Ajouter"
    And le formulaire de création s ouvre avec le titre "Créer un nouvel utilisateur"
    And je saisis les informations d utilisateur :
      | matricule | <matricule> |
      | nom       | <nom>       |
      | prenom    | <prenom>    |
      | email     | <email>     |
      | password  | <password>  |
      | role      | <role>      |
      | image     | <image>     |
    And j appui sur "Créer Utilisateur"
    Then SweetAlert d erreur avec le titre "<titre>" et le message "<message>" s affiche sur la page de création de l utilisateur

    Examples: 
      | matricule | nom   | prenom | email             | password | role   | image        | titre  | message                       |
      | ala       | Sami  | Sami   | ala.new@gmail.com | Ala@123* | MEMBER | image 2.jpeg | Erreur | Le matricule existe déjà!     |
      | user3      | Ahmed | Ahmed  | ala@gmail.com     | Ala@123* | MEMBER | image 2.jpeg | Erreur | Erreur : Email déjà utilisé ! |

  @TC-04-CreateUser-ErrorRequiredEmpty @p2 @ui @functional @negative @validation @creation @error_champ_vide
  Scenario Outline: Champ obligatoire vide lors de la création
    When j appui sur "Ajouter"
    And le formulaire de création s ouvre avec le titre "Créer un nouvel utilisateur"
    And je saisis les informations d utilisateur :
      | matricule | <matricule> |
      | nom       | <nom>       |
      | prenom    | <prenom>    |
      | email     | <email>     |
      | password  | <password>  |
      | role      | <role>      |
      | image     | <image>     |
    Then le bouton "Créer Utilisateur" est désactivé sur le formulaire de création

    Examples: 
      | matricule | nom   | prenom | email          | password | role    | image        |
      |           | Dupon | John   | test@gmail.com | John123* | MANAGER | image 1.jpeg |
      | sami      |       | Sami   | test@gmail.com | Sami123* | MEMBER  | image 2.jpeg |
