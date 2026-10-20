@update_user
Feature: Gestion des comptes — Modification d’utilisateurs (CoreLink)
  En tant qu’administrateur, je veux modifier totalement ou partiellement les informations d’un utilisateur,
  afin de maintenir les données à jour et assurer la cohérence des comptes.

  Background: 
    Given je suis connecté en tant qu administrateur
    And un utilisateur actif auto-cree

  @TC-01-UpdateUser-FullSuccess @p1 @ui @functional @smoke @modification_complete
  Scenario: Modification complète de l utilisateur existant
    And je sélectionne l utilisateur courant et je clique sur l icône "modifier"
    And le formulaire de modification s ouvre pré-rempli
    And je modifie les informations d utilisateur :
      | nom    | <auto>       |
      | prenom | <auto>       |
      | email  | <auto>       |
      | role   | MEMBER       |
      | image  | image 2.jpeg |
    And je confirme la modification en cliquant sur "Mettre à jour"
    Then Succes Alerte avec le titre "Succès" et le message "Utilisateur mis à jour avec succès" s affiche
    And je suis redirigé vers la liste des utilisateurs
    And je souhaite chercher l utilisateur
    Then je souhaite voir les memes informations :
      | nom    | <auto>       |
      | prenom | <auto>       |
      | email  | <auto>       |
      | role   | MEMBER       |
      | image  | image 2.jpeg |

  @TC-02-UpdateUser-PartialSuccess @p2 @ui @functional @modification_partielle
  Scenario: Modification partielle de l utilisateur existant (email uniquement)
    And je sélectionne l utilisateur courant et je clique sur l icône "modifier"
    And le formulaire de modification s ouvre pré-rempli
    And je modifie les informations d utilisateur :
      | email | <auto> |
    And je confirme la modification en cliquant sur "Mettre à jour"
    Then Succes Alerte avec le titre "Succès" et le message "Utilisateur mis à jour avec succès" s affiche
    And je suis redirigé vers la liste des utilisateurs
    And je souhaite chercher l utilisateur
    Then je souhaite voir les memes informations :
      | email | <auto> |

  @TC-03-UpdateUser-NoChangeConfirm @p3 @ui @functional @edge @modification_sans_changement_confimation
  Scenario Outline: Modification sans changement
    And je sélectionne l utilisateur courant et je clique sur l icône "modifier"
    And le formulaire de modification s ouvre pré-rempli
    And je confirme la modification en cliquant sur "Mettre à jour"
    Then confirmation alerte avec le titre "<titre>" et le message "<message>" s affiche
    And je souhaite quitter la page
    Then je suis redirigé vers la liste des utilisateurs

    Examples: 
      | titre                    | message                                                      |
      | Aucun changement détecté | Vous n’avez rien modifié. Êtes-vous sûr de vouloir quitter ? |

  @TC-04-UpdateUser-NoChangeCancel @p3 @ui @functional @edge @modification_sans_changement_annulation
  Scenario Outline: Modification sans changement
    And je sélectionne l utilisateur courant et je clique sur l icône "modifier"
    And le formulaire de modification s ouvre pré-rempli
    And je confirme la modification en cliquant sur "Mettre à jour"
    Then confirmation alerte avec le titre "<titre>" et le message "<message>" s affiche
    And je souhaite rester sur la page actuelle
    Then je souhaite voir le titre de la page "Modifier l'utilisateur"

    Examples: 
      | titre                    | message                                                      |
      | Aucun changement détecté | Vous n’avez rien modifié. Êtes-vous sûr de vouloir quitter ? |

  @TC-05-UpdateUser-MatriculeNotEditable @p1 @ui @functional @negative @modification_matricule_non_editable
  Scenario: Le champ matricule est non modifiable
    And je sélectionne l utilisateur courant et je clique sur l icône "modifier"
    Then le champ "matricule" est désactivé et non modifiable

  @TC-06-UpdateUser-ErrorDuplicateEmail @p1 @ui @functional @negative @validation @error @doublon
  Scenario Outline: Erreur de duplication d email lors de la modification
    And je sélectionne l utilisateur courant et je clique sur l icône "modifier"
    And le formulaire de modification s ouvre pré-rempli
    And je modifie les informations d utilisateur :
      | email | <email> |
    And je confirme la modification en cliquant sur "Mettre à jour"
    Then Error alerte avec le titre "<titre>" et le message "<message>" s affiche

    Examples: 
      | email              | titre  | message                                              |
      | sami.new@gmail.com | Erreur | Erreur lors de la mise à jour : Email déjà utilisé ! |

  @TC-07-UpdateUser-CancelReturnList @p3 @ui @functional @revenir_liste_sans_modification
  Scenario: Annulation de la modification (retour à la liste)
    And je sélectionne l utilisateur courant et je clique sur l icône "modifier"
    And le formulaire de modification s ouvre pré-rempli
    And je souhaite revenir a la liste
    And je souhaite chercher l utilisateur
    Then je vois l utilisateur courant dans la liste
