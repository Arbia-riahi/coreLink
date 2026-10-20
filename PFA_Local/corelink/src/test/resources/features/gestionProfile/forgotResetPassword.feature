@MotDePasseOublie
Feature: Gestion du mot de passe oublié
  
  En tant qu’utilisateur,
  je veux pouvoir réinitialiser mon mot de passe afin de protéger l’accès à mon compte et garantir la sécurité de mes informations.

  Background: 
    Given je suis sur la page de connexion
    And que je clique sur "Mot de passe oublié ?"

  @HappyPath
  Scenario Outline: Réinitialisation réussie du mot de passe pour un utilisateur existant
    And que je saisis l’adresse e-mail "<email>"
    When que je clique sur "Envoyer le lien"
    Then un message de confirmation apparaît avec le titre "lien envoyé" et le message "Un email de réinitialisation a été envoyé à {email}"
    And il clique sur le lien de réinitialisation dans cet email
    And qu’il saisit un nouveau mot de passe "<nouveau mot de passe>"
    And qu’il confirme le mot de passe dans le champ "<confirmer nouveau mot de passe>"
    And qu’il clique sur "Réinitialiser"
    Then une alerte de succès s affiche avec le titre "Succès" et le message indique "Mot de passe réinitialisé avec succès."
    And je suis sur la page de connexion

    Examples: 
      | email              | nouveau mot de passe | confirmer nouveau mot de passe |
      | mailsac.test.email | newPassword          | newPassword                    |

  @InvalidEmail
  Scenario Outline: Attempt to reset password with unregistered email
    And que je saisis l’adresse e-mail inexistante "<email>"
    When que je clique sur "Envoyer le lien"
    Then une alerte d erreur s affiche avec le titre "Erreur" et le message indique "Erreur : Aucun utilisateur trouvé avec cet email."

    Examples: 
      | email         |
      | edx@gmail.com |

  @EmptyField
  Scenario: Submit forgot password form with empty email field
    When que je clique sur "Envoyer le lien"
    Then une alerte d avertissement s affiche avec le titre "Email requis" et le message indique "Merci de saisir votre email."

  @WeakPassword
  Scenario Outline: New password does not meet security requirements
    And que je saisis l’adresse e-mail "<email>"
    When que je clique sur "Envoyer le lien"
    Then un message de confirmation apparaît avec le titre "lien envoyé" et le message "Un email de réinitialisation a été envoyé à {email}"
    And il clique sur le lien de réinitialisation dans cet email
    And qu’il saisit un nouveau mot de passe faible "<nouveau mot de passe>"
    And qu’il confirme le mot de passe faible dans le champ "<confirmer nouveau mot de passe>"
    And qu’il clique sur "Réinitialiser"
    Then une alerte d avertissement s affiche avec le titre "Mot de passe faible" et le message indique "Utilisez au moins 8 caractères avec majuscule, minuscule, chiffre et symbole."

    Examples: 
      | email              | nouveau mot de passe | confirmer nouveau mot de passe |
      | mailsac.test.email |                  123 |                            123 |

  @MismatchPassword
  Scenario Outline: New password and confirmation do not match
    And que je saisis l’adresse e-mail "<email>"
    When que je clique sur "Envoyer le lien"
    Then un message de confirmation apparaît avec le titre "lien envoyé" et le message "Un email de réinitialisation a été envoyé à {email}"
    And il clique sur le lien de réinitialisation dans cet email
    And qu’il saisit un premier nouveau mot de passe "<nouveau mot de passe>"
    And qu’il saisit autre mot de passe dans "<confirmer nouveau mot de passe>"
    And qu’il clique sur "Réinitialiser"
    Then une alerte d avertissement s affiche avec le titre "Attention" et le message indique "Les mots de passe ne correspondent pas."

    Examples: 
      | email              | nouveau mot de passe | confirmer nouveau mot de passe |
      | mailsac.test.email |                  123 |                       12345678 |

  @ExpiredLink
  Scenario: Reset password link expired
    And que je saisis l’adresse e-mail "<email>"
    When que je clique sur "Envoyer le lien"
    Then un message de confirmation apparaît avec le titre "lien envoyé" et le message "Un email de réinitialisation a été envoyé à {email}"
    And je souhaite sur le lien de réinitialisation déja expiré
    Then une alerte d erreur s affiche avec le titre "Lien invalide" et le message indique "Le lien de réinitialisation a expiré"
