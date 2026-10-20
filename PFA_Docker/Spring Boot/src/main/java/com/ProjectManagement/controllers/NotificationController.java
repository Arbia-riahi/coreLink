package com.ProjectManagement.controllers;

import com.ProjectManagement.DTO.NotificationDTO;
import com.ProjectManagement.services.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

	private final NotificationService notificationService;

	public NotificationController(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	// ============================================================
	// RÉCUPÉRER TOUTES LES NOTIFICATIONS
	// ============================================================
	@Operation(summary = "Lister toutes les notifications d’un utilisateur", description = """
			Retourne **toutes** les notifications d’un utilisateur (lues + non lues).

			🔎 **Tests QA recommandés :**
			- Utilisateur avec notifications → liste non vide
			- Utilisateur sans notifications → liste vide
			- Vérifier ordre (souvent DESC par date)
			- Vérifier cohérence NotificationDTO : message, date, type, état (read / unread)
			""")
	@ApiResponse(responseCode = "200", description = "Notifications renvoyées", content = @Content(array = @ArraySchema(schema = @Schema(implementation = NotificationDTO.class))))
	@GetMapping("/{matricule}")
	public ResponseEntity<List<NotificationDTO>> getNotificationsByUser(@PathVariable String matricule) {
		List<NotificationDTO> notifications = notificationService.getNotificationsByUser(matricule);
		return ResponseEntity.ok(notifications);
	}

	// ============================================================
	// 2️⃣ RÉCUPÉRER NOTIFICATIONS NON LUES
	// ============================================================
	@Operation(summary = "Lister uniquement les notifications non lues", description = """
			Retourne uniquement les notifications dont l’utilisateur n’a pas encore pris connaissance.

			🔎 **Tests QA recommandés :**
			- Vérifier count avec /unreadCount
			- Vérifier cohérence du statut read = false
			- Déclencher une nouvelle notification ailleurs (commentaire, tâche…) → vérifier apparition ici
			""")
	@ApiResponse(responseCode = "200", description = "Notifications non lues renvoyées", content = @Content(array = @ArraySchema(schema = @Schema(implementation = NotificationDTO.class))))
	@GetMapping("/{matricule}/unread")
	public ResponseEntity<List<NotificationDTO>> getUnreadNotifications(@PathVariable String matricule) {
		List<NotificationDTO> unread = notificationService.getUnreadNotifications(matricule);
		return ResponseEntity.ok(unread);
	}

	// ============================================================
	// 3️⃣ MARQUER UNE NOTIFICATION COMME LUE
	// ============================================================
	@Operation(summary = "Marquer une notification comme lue", description = """
			Met le statut d’une notification sur **read = true**.

			🔎 **Tests QA :**
			- Après lecture → vérifier qu’elle n’apparait plus dans `/unread`
			- Vérifier retour NotificationDTO mis à jour
			- Cas notification inexistante → vérifier comportement service
			""")
	@ApiResponse(responseCode = "200", description = "Notification marquée comme lue", content = @Content(schema = @Schema(implementation = NotificationDTO.class)))
	@PatchMapping("/{id}/read")
	public ResponseEntity<NotificationDTO> markAsRead(@PathVariable Long id) {
		NotificationDTO updated = notificationService.markAsRead(id);
		return ResponseEntity.ok(updated);
	}

	// ============================================================
	// 4️⃣ MARQUER TOUTES LES NOTIFICATIONS COMME LUES
	// ============================================================
	@Operation(summary = "Marquer toutes les notifications comme lues", description = """
			Met l’ensemble des notifications utilisateur sur **read = true**.

			🔎 **Tests QA :**
			- Avant → vérifier count > 0 avec /unreadCount
			- Après → count = 0
			- Vérifier que l’historique reste présent dans /{matricule}
			""")
	@ApiResponse(responseCode = "204", description = "Toutes les notifications marquées comme lues")
	@PatchMapping("/{matricule}/read-all")
	public ResponseEntity<Void> markAllAsRead(@PathVariable String matricule) {
		notificationService.markAllAsRead(matricule);
		return ResponseEntity.noContent().build();
	}

	// ============================================================
	// 5️⃣ SUPPRIMER UNE NOTIFICATION
	// ============================================================
	@Operation(summary = "Supprimer une notification", description = """
			Supprime une notification unique.

			🔎 **Tests QA :**
			- Supprimer une notif lue → ok
			- Supprimer une notif non lue → vérifier count
			- Supprimer notif inexistante → comportement service (à documenter)
			""")
	@ApiResponse(responseCode = "204", description = "Notification supprimée")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
		notificationService.deleteNotification(id);
		return ResponseEntity.noContent().build();
	}

	// ============================================================
	// 6️⃣ NOMBRE DE NOTIFICATIONS NON LUES
	// ============================================================
	@Operation(summary = "Obtenir le nombre de notifications non lues", description = """
			Retourne **uniquement le nombre** de notifications non lues pour un utilisateur.

			🔎 **Tests QA recommandés :**
			- Générer différentes notifications → vérifier synchronisation
			- Après appel /read-all → résultat doit être 0
			- Vérifier cohérence avec /unread
			""")
	@ApiResponse(responseCode = "200", description = "Nombre de notifications non lues renvoyé", content = @Content(schema = @Schema(example = "5")))
	@GetMapping("/{matricule}/unreadCount")
	public ResponseEntity<Long> getUnreadCount(@PathVariable String matricule) {
		long count = notificationService.countUnreadNotifications(matricule);
		return ResponseEntity.ok(count);
	}

}