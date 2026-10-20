package com.ProjectManagement.controllers;

import com.ProjectManagement.DTO.*;
import com.ProjectManagement.entities.*;
import com.ProjectManagement.services.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/commentaires")
@CrossOrigin(origins = "*")
public class CommentaireController {

	private final CommentaireService commentaireService;
	private final NotificationService notificationService;
	private final TacheService tacheService;
	private final UserDetailsServiceImpl userService;

	public CommentaireController(CommentaireService commentaireService, NotificationService notificationService,
			TacheService tacheService, UserDetailsServiceImpl userService) {
		this.commentaireService = commentaireService;
		this.notificationService = notificationService;
		this.tacheService = tacheService;
		this.userService = userService;
	}

	// RÉCUPÉRER COMMENTAIRES D'UNE TÂCHE
	@Operation(summary = "Lister les commentaires d'une tâche", description = """
			Retourne tous les commentaires (et sous-commentaires) liés à une tâche.

			🔎 **Tests QA recommandés :**
			- Tâche existante → 200
			- Tâche inexistante → liste vide ou erreur selon service
			- Vérifier structure : auteur, date, contenu, parentCommentId
			- Vérifier ordre d’affichage (si logique définie)
			""")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Liste des commentaires renvoyée", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CommentaireDTO.class)))) })
	@GetMapping("/tache/{tacheId}")
	public ResponseEntity<List<CommentaireDTO>> getCommentairesByTache(@PathVariable Long tacheId) {
		return ResponseEntity.ok(commentaireService.getCommentairesDTOByTache(tacheId));
	}

	// ============================================================
	// AJOUTER COMMENTAIRE / RÉPONSE
	// ============================================================
	@Operation(summary = "Ajouter un commentaire (ou réponse) à une tâche", description = """
			Ajoute un commentaire sur une tâche.

			✔ **Type : JSON**
			✔ Peut créer un sous-commentaire via parentCommentId
			✔ Déclenche **des notifications automatiques** :

			🔔 **Cas QA à tester :**
			- Nouveau commentaire → Notification NOUVEAU_COMMENTAIRE
			- Réponse à un commentaire → Notification REPONSE_COMMENTAIRE
			- Mention @matricule → Notification MENTION_DANS_COMMENTAIRE
			- Champs obligatoires manquants → 400
			- Tâche inexistante → 404 (selon service)

			Exemple JSON :
			```
			{
			  "matriculeAuteur": "A100",
			  "contenu": "Je valide ce point.",
			  "parentCommentId": 5
			}
			```
			""")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Commentaire créé", content = @Content(schema = @Schema(implementation = CommentaireDTO.class))),
			@ApiResponse(responseCode = "400", description = "Champs invalides") })
	@PostMapping("/tache/{tacheId}/add")
	public ResponseEntity<CommentaireDTO> ajouterCommentaire(@PathVariable Long tacheId,
			@RequestBody Map<String, String> payload) {

		String matriculeAuteur = payload.get("matriculeAuteur");
		String contenu = payload.get("contenu");
		Long parentCommentId = payload.containsKey("parentCommentId") ? Long.parseLong(payload.get("parentCommentId"))
				: null;

		if (matriculeAuteur == null || matriculeAuteur.isEmpty() || contenu == null || contenu.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}

		// Ajout du commentaire
		CommentaireDTO newComment = commentaireService.ajouterCommentaire(tacheId, matriculeAuteur, contenu,
				parentCommentId);

		// --- Récupération de la tâche et de l'auteur (entités) ---
		Tache tache = tacheService.getTacheById(tacheId); // Retourne l'entité Tache
		User auteur = userService.getUserByMatricule(matriculeAuteur);

		// --- NOTIFICATIONS ---
		if (parentCommentId != null) {
			// Réponse à un commentaire existant
			notificationService.creerNotificationPourMembresTache(tache, auteur,
					"Nouvelle réponse sur la tâche '" + tache.getTitre() + "' : " + contenu,
					TypeNotification.REPONSE_COMMENTAIRE);
		} else {
			// Nouveau commentaire
			notificationService.creerNotificationPourMembresTache(tache, auteur,
					"Nouveau commentaire sur la tâche '" + tache.getTitre() + "' : " + contenu,
					TypeNotification.NOUVEAU_COMMENTAIRE);
		}

		// --- Vérification des mentions @matricule ---
		if (tache.getAutresAssignes() != null) {
			for (User membre : tache.getAutresAssignes()) {
				if (contenu.contains("@" + membre.getMatricule())) {
					notificationService.creerNotification(membre,
							"Vous avez été mentionné dans un commentaire sur la tâche '" + tache.getTitre() + "'.",
							TypeNotification.MENTION_DANS_COMMENTAIRE, tache.getProjet(), tache);
				}
			}
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(newComment);
	}

	// ============================================================
	// MODIFIER UN COMMENTAIRE
	// ============================================================
	@Operation(summary = "Modifier un commentaire existant", description = """
			Permet de modifier le contenu d’un commentaire.

			✔ **Type : JSON**
			✔ Champs obligatoires :
			  - contenu

			🔎 **Tests QA recommandés :**
			- Commentaire existant → 200
			- Contenu vide → 400
			- Vérifier que l’historique n’est pas conservé (sauf si logique prévue)
			""")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Commentaire modifié"),
			@ApiResponse(responseCode = "400", description = "Contenu vide") })
	@PutMapping("/{commentaireId}")
	public ResponseEntity<CommentaireDTO> modifierCommentaire(@PathVariable Long commentaireId,
			@RequestBody Map<String, String> payload) {

		String nouveauContenu = payload.get("contenu");
		if (nouveauContenu == null || nouveauContenu.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.ok(commentaireService.modifierCommentaire(commentaireId, nouveauContenu));
	}

	// ============================================================
	// SUPPRIMER UN COMMENTAIRE
	// ============================================================
	@Operation(summary = "Supprimer un commentaire", description = """
			Supprime un commentaire par son ID.

			🔎 **Tests QA recommandés :**
			- Commentaire existant → 204
			- Commentaire inexistant → tester si service gère une exception
			- Vérifier suppression de sous-commentaires (si logique prévue)
			""")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Supprimé avec succès") })
	@DeleteMapping("/{commentaireId}")
	public ResponseEntity<Void> supprimerCommentaire(@PathVariable Long commentaireId) {
		commentaireService.supprimerCommentaire(commentaireId);
		return ResponseEntity.noContent().build();
	}
}