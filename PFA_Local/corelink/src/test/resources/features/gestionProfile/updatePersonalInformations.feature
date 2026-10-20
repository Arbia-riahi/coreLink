@Modifier_personal_Informations
Feature: Gestion du profil utilisateur — Modification des informations personnelles
  En tant qu’utilisateur, je veux mettre à jour mes informations personnelles ,
  afin de garder mon profil complet et sécurisé.

  #houni me7atitch background 5ater table 'Examples' metjich m3a background -> tji ken m3a sc outline
  @TC-01-ProfileUpdate-FullSuccess @p1 @ui @functional @smoke
  Scenario Outline: Modification complète de mes données personelles
    Given je suis authentifiée tant que "<role>"
    Then je clique sur le menu utilsateur pour consulter mon profile
    And je valide avec "Mon Profile"
    And un formulaire pré-rempli contenat mes informations personelles
    And je modifie mes informations personelles:
      | nom    | <auto>       |
      | prenom | <auto>       |
      | email  | <auto>       |
      | image  | image 3.jpeg |
    And je valide avec "Mettre à jour le profil"
    Then une alerte Succes dont le titre est "Succès" et le message est "Profil mis à jour avec succès" s affiche
    And je vois mes donnes changé avec :
      | nom    | <auto>       |
      | prenom | <auto>       |
      | email  | <auto>       |
      | image  | image 3.jpeg |

    Examples: 
      | role    |
      | MANAGER |
      | ADMIN   |
      | MEMBER  |

  @TC-02-ProfileUpdate-PartialSuccess @p2 @ui @functional @modification_partielle
  Scenario Outline: Modification partielle
    Given je suis authentifiée tant que "<role>"
    Then je clique sur le menu utilsateur pour consulter mon profile
    And je valide avec "Mon Profile"
    And un formulaire pré-rempli contenat mes informations personelles
    And je modifie mes informations personelles:
      | prenom | <auto> |
      | email  | <auto> |
    And je valide avec "Mettre à jour le profil"
    Then une alerte Succes dont le titre est "Succès" et le message est "Profil mis à jour avec succès" s affiche
    And je vois mes donnes changé avec :
      | prenom | <auto> |
      | email  | <auto> |

    Examples: 
      | role    |
      | ADMIN   |
      | MANAGER |
      | MEMBER  |

  @TC-03-ProfileUpdate-NoChangeConfirm @p3 @functional @edge @Confirmation_modification_sans_changement
  Scenario Outline: Modification sans changement - confirmation
    Given je suis authentifiée tant que "<role>"
    Then je clique sur le menu utilsateur pour consulter mon profile
    And je valide avec "Mon Profile"
    And un formulaire pré-rempli contenat mes informations personelles
    And je valide avec "Mettre à jour le profil"
    Then une alerte pour confirmer dont le titre est "Aucune modification" et le message est "Vous n'avez rien changé. Voulez-vous quitter la page ?" s affiche
    And je confirme la fermeture de la page courante
    Then je suis redigé vers l espace "<espace>" reservé pour chaque utilisateur

    Examples: 
      | role    | espace                |
      | ADMIN   | Espace Administrateur |
      | MANAGER | Espace Chef de Projet |
      | MEMBER  | Espace collaborateurs |

  @TC-04-ProfileUpdate-NoChangeCancel @p3 @functional @edge @Annulation_modification_sans_changement
  Scenario Outline: Modification sans changement - annulation
    Given je suis authentifiée tant que "<role>"
    Then je clique sur le menu utilsateur pour consulter mon profile
    And je valide avec "Mon Profile"
    And un formulaire pré-rempli contenat mes informations personelles
    And je valide avec "Mettre à jour le profil"
    Then une alerte pour confirmer dont le titre est "Aucune modification" et le message est "Vous n'avez rien changé. Voulez-vous quitter la page ?" s affiche
    And j annule la fermeture de la page courante
    And je souhaite voir le texte "Mon profil"

    Examples: 
      | role    |
      | ADMIN   |
      | MANAGER |
      | MEMBER  |

  @TC-05-ProfileUpdate-MatriculeNotEditable @p2 @ui @functional @negative @modification_matricule_non_editable
  Scenario Outline: Le champ matricule est non modifiable
    Given je suis authentifiée tant que "<role>"
    Then je clique sur le menu utilsateur pour consulter mon profile
    And je valide avec "Mon Profile"
    And un formulaire pré-rempli contenat mes informations personelles
    Then le champ "matricule" est visible , non cliquable et non modifiable

    Examples: 
      | role    |
      | ADMIN   |
      | MANAGER |
      | MEMBER  |
  #@TC-05-ProfileUpdate-ErrorDuplicateEmail @p1 @ui @functional @error @negative @doublon
  #Scenario Outline: Erreur de duplication d email lors de la modification
  # Given je suis authentifiée tant que "<role>"
  # Then je clique sur le menu utilsateur pour consulter mon profile
  # And je valide avec "Mon Profile"
  #And un formulaire pré-rempli contenat mes informations personelles
  #And je modifie mes informations personelles:
  # | email | <email> |
  #And je valide avec "Mettre à jour le profil"
  #Then Error alerte avec "<titre>" et "<message>" s affiche
  # Examples: 
  #  | role    | email            | titre  | message                                             |
  #| ADMIN   | azizos@gmail.com | Erreur | Cet email existe déjà! Veuillez en choisir un autre |
  # | MANAGER | azizos@gmail.com | Erreur | Cet email existe déjà! Veuillez en choisir un autre |
  #| MEMBER  | azizos@gmail.com | Erreur | Cet email existe déjà! Veuillez en choisir un autre |
