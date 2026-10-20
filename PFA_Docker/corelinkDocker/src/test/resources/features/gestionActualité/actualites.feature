@domain.actualites
Feature: Gestion des actualités — Création, Modification, Suppression & Pagination (CoreLink)
  En tant qu’administrateur, je veux créer, modifier, supprimer et parcourir les actualités,
  afin de diffuser et gérer efficacement les informations.

  Background: 
    Given je m authentifié en tant que admin

  @TC-01-CreateNews-Success @functional @creation_success
  Scenario: Création une nouvelle actualité avec succès
    And je choisi de presser sur "Raccourcis"
    And je choisi de presser sur "Actualités"
    And je choisi de presser sur "Ajouter"
    And le formulaire de création vierge est bien visible sur la page
    And je saisis les informations relatives a une actualité :
      | titre       | <auto>                                                                        |
      | description | <auto>                                                                        |
      | photos      | image 1.jpeg, image 2.jpeg, exception 1.jpg, exception 2.jpg, exception 3.jpg |
    And je choisi de presser sur "Créer l’actualité"
    Then SweetAlert de succès avec le titre "Succès" et le message "Votre actualité a été créée avec succès" sera visible
    And je suis envoyé vers la liste des actualités et je vois une actualités avec :
      | titre       | <auto>                                                                        |
      | description | <auto>                                                                        |
      | photos      | image 1.jpeg, image 2.jpeg, exception 1.jpg, exception 2.jpg, exception 3.jpg |

  @TC-02-CreateNews-ErrorFormatImage @functional @negative @validation @creation @error_format_image
  Scenario Outline: Erreurs de format images lors de la création d une actualité
    And je choisi de presser sur "Raccourcis"
    And je choisi de presser sur "Actualités"
    And je choisi de presser sur "Ajouter"
    And le formulaire de création vierge est bien visible sur la page
    And je saisis les informations relatives a une actualité :
      | titre       | <auto>   |
      | description | <auto>   |
      | photos      | <photos> |
    And je choisi de presser sur "Créer l’actualité"
    Then SweetAlert d erreur avec le titre "Erreur" et le message "• Seuls les fichiers de type image (JPG, PNG, GIF, WEBP) sont autorisés" sera visible

    Examples: 
      | titre  | description | photos            |
      | <auto> | <auto>      | cas de tests.xlsx |
      | <auto> | <auto>      | BDD.pdf           |

  @TC-03-CreateNews-ErrorRequiredEmpty @functional @negative @validation @creation @error_champ_vide
  Scenario Outline: Champ obligatoire vide lors de la création
    And je choisi de presser sur "Raccourcis"
    And je choisi de presser sur "Actualités"
    And je choisi de presser sur "Ajouter"
    And le formulaire de création vierge est bien visible sur la page
    And je saisis les informations relatives a une actualité :
      | titre       | <titre>       |
      | description | <description> |
      | photos      | <photos>      |
    Then le bouton de création sera désactivé

    Examples: 
      | titre     | description | photos       |
      | actualité | ghheyy      |              |
      |           | hello title | image 1.jpeg |
      | actualité |             | image 2.jpeg |

  ##################update##############################
  @TC-04-UpdateNews-FullSuccess @modification_complete
  Scenario: Modification complète d une actualite
    And une actualité actif auto-cree
    And je suis sur la page "Gestion des Actualités"
    And je sélectionne l actualite courant et je clique sur l icône "modifier"
    And le formulaire de modification sera ouvert pré-rempli
    And je modifie les informations relatives a une actualité:
      | titre       | <auto>                                                       |
      | description | <auto>                                                       |
      | photos      | image 1.jpeg, image 2.jpeg, exception 1.jpg, exception 2.jpg |
    And je choisi de presser sur "Mettre à jour l'actualité"
    Then SweetAlert de succès avec le titre "Actualité mise à jour" et le message "Les modifications ont été enregistrées avec succès" sera visible
    And je suis envoyé vers la liste des actualités et je vois une actualités avec :
      | titre       | <auto>                                                       |
      | description | <auto>                                                       |
      | photos      | image 1.jpeg, image 2.jpeg, exception 1.jpg, exception 2.jpg |

  @TC-05-UpdateNews-PartialSuccess @p2 @ui @functional @modification_partielle
  Scenario: Modification partielle
    And une actualité actif auto-cree
    And je suis sur la page "Gestion des Actualités"
    And je sélectionne l actualite courant et je clique sur l icône "modifier"
    And le formulaire de modification sera ouvert pré-rempli
    And je modifie les informations relatives a une actualité:
      | titre | <auto> |
    And je choisi de presser sur "Mettre à jour l'actualité"
    Then SweetAlert de succès avec le titre "Actualité mise à jour" et le message "Les modifications ont été enregistrées avec succès" sera visible
    And je suis envoyé vers la liste des actualités et je vois une actualités avec :
      | titre | <auto> |

  @TC-06-UpdateNews-NoChangeConfirm @p3 @ui @functional @edge @modification_sans_changement_OK
  Scenario: Modification sans changement avec retour a la liste des actualités
    And une actualité actif auto-cree
    And je suis sur la page "Gestion des Actualités"
    And je sélectionne l actualite courant et je clique sur l icône "modifier"
    And le formulaire de modification sera ouvert pré-rempli
    And je choisi de presser sur "Mettre à jour l'actualité"
    Then SweetAlert de confirmation avec le titre "Aucun changement détecté" et le message "Vous n’avez rien modifié. Êtes-vous sûr de vouloir quitter ?" sera visible
    And je valide la fermeture de la page
    And je suis sur la page "Gestion des Actualités"

  @TC-07-UpdateNews-NoChangeCancel @p3 @ui @functional @edge @modification_sans_changement_non
  Scenario: Modification sans changement sans retour a la liste des actualites
    And une actualité actif auto-cree
    And je suis sur la page "Gestion des Actualités"
    And je sélectionne l actualite courant et je clique sur l icône "modifier"
    And le formulaire de modification sera ouvert pré-rempli
    And je choisi de presser sur "Mettre à jour l'actualité"
    Then SweetAlert de confirmation avec le titre "Aucun changement détecté" et le message "Vous n’avez rien modifié. Êtes-vous sûr de vouloir quitter ?" sera visible
    And j annule la fermeture de la page
    And je reste sur la page "Modifier une actualité !"

  ###################suppression############################################
  @TC-08-DeleteNews-Cancel @p2 @functional @delete_annulee
  Scenario: Annulation de suppresson d'une actualité existante
    And une actualité actif auto-cree
    And je suis sur la page "Gestion des Actualités"
    And je sélectionne l actualite courant et je clique sur l icône "supprimer"
    Then SweetAlert de confirmation avec le titre "Êtes-vous sûr ?" et le message "Cette action est irréversible !" sera visible
    And j annule la supression
    And je persiste sur la page des actualités et je vois que les informations sont persistés :
      | titre       | <auto>                                                       |
      | description | <auto>                                                       |
      | photos      | image 1.jpeg, image 2.jpeg, exception 1.jpg, exception 2.jpg |

  @TC-09-DeleteNews-Confirm @p1 @functional @delete_confirmee
  Scenario: suppresion d'une actualite existante
    And une actualité actif auto-cree
    And je suis sur la page "Gestion des Actualités"
    And je sélectionne l actualite courant et je clique sur l icône "supprimer"
    Then SweetAlert de confirmation avec le titre "Êtes-vous sûr ?" et le message "Cette action est irréversible !" sera visible
    And je confirme la supression
    Then SweetAlert de succès avec le titre "Supprimé !" et le message "L’actualité a été supprimée avec succès" sera visible
    And je ne vois plus l actualite dans la liste

  ################################pagination#####################
  @TC-10-NewsPagination-RowsPerPage @p3 @functional @pagination @nbrRowsPerPage
  Scenario: pagination_5_per_page
    And je choisi de presser sur "Raccourcis"
    And je choisi de presser sur "Actualités"
    And je suis sur la page "Gestion des Actualités"
    And je vois exactement 5 lignes dans une page

  @TC-11-NewsPagination-NextPrev @p2 @ui @functional @pagination @navigateNextPrev
  Scenario: Navigation Suivant / Précédent
    And je choisi de presser sur "Raccourcis"
    And je choisi de presser sur "Actualités"
    And je suis sur la page 1 des actualitées
    And je mémorise le titre de la 1ère ligne comme "firstPageRow1"
    And je choisi de presser sur "Suivant"
    Then le titre de la 1 ère ligne est différent de "firstPageRow1"
    And je choisi de presser sur "Précédent"
    Then le titre de la 1 ère ligne est égal à "firstPageRow1"

  @TC-12-NewsPagination-FirstLastDisabled @p3 @ui @functional @pagination @firstLastDisabled
  Scenario: Désactivation en première et dernière page
    And je choisi de presser sur "Raccourcis"
    And je choisi de presser sur "Actualités"
    And je suis sur la page "Gestion des Actualités"
    And je navigue vers la dernière page
