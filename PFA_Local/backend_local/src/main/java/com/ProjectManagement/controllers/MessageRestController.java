package com.ProjectManagement.controllers;

import com.ProjectManagement.DTO.MessageDTO;
import com.ProjectManagement.DTO.MemberDTO;
import com.ProjectManagement.services.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin("*")
public class MessageRestController {

	private static final String UPLOAD_DIR = "src/main/resources/static/uploads/audio/";

	@Autowired
	private MessageService messageService;

	// ============================================================
	// 1️⃣ HISTORIQUE MESSAGES
	// ============================================================
	@Operation(summary = "Récupérer l'historique des messages", description = """
			Retourne l'historique complet des messages du système.

			🔎 **Tests QA recommandés :**
			- Vérifier ordre (souvent par date croissante ou décroissante)
			- Messages avec fichier audio
			- Messages avec fichiers images (si intégrés ailleurs)
			- Messages "lus" vs "non lus"
			- Vérifier structure MessageDTO

			Format : JSON Array
			""")
	@ApiResponse(responseCode = "200", description = "Historique récupéré", content = @Content(array = @ArraySchema(schema = @Schema(implementation = MessageDTO.class))))
	@GetMapping("/history")
	public List<MessageDTO> getMessageHistory() {
		return messageService.getAllMessages();
	}

	// ============================================================
	// 2️⃣ MARQUER MESSAGE COMME LU
	// ============================================================
	@Operation(summary = "Marquer un message comme lu", description = """
			Met à jour le statut d'un message pour signaler qu'un utilisateur l'a lu.

			✔ Paramètres :
			  - messageId (Path)
			  - readerMatricule (Query)

			🔎 **Tests QA recommandés :**
			- Message existant → 200 OK
			- Message inexistant → vérifier comportement service
			- ReaderMatricule invalide → 400 ou erreur logique
			- Vérifier que le message apparaît dans /seen-by

			Exemple :
			```
			PUT /api/messages/45/read?readerMatricule=A100
			```
			""")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Message marqué comme lu"), })
	@PutMapping("/{messageId}/read")
	public ResponseEntity<?> markMessageAsRead(@PathVariable Long messageId, @RequestParam String readerMatricule) {
		messageService.markMessageAsRead(messageId, readerMatricule);
		return ResponseEntity.ok().build();
	}

	// ============================================================
	// 3️⃣ LISTE DES UTILISATEURS QUI ONT VU LE MESSAGE
	// ============================================================
	@Operation(summary = "Liste des utilisateurs ayant vu le message", description = """
			Retourne la liste des membres ayant vu un message spécifique.

			🔎 **Tests QA recommandés :**
			- Message existant → liste non vide
			- Message nouveau → liste vide
			- QA doit vérifier que les mêmes utilisateurs sont synchronisés avec WebSocket (si applicable)

			Format retour : List<MemberDTO>
			""")
	@ApiResponse(responseCode = "200", description = "Liste des membres renvoyée", content = @Content(array = @ArraySchema(schema = @Schema(implementation = MemberDTO.class))))
	@GetMapping("/{messageId}/seen-by")
	public List<MemberDTO> getSeenBy(@PathVariable Long messageId) {
		return messageService.getSeenBy(messageId);
	}

	// ============================================================
	// 4️⃣ COMPTE DES MESSAGES NON LUS
	// ============================================================
	@Operation(summary = "Compter les messages non lus d'un utilisateur", description = """
			Retourne le nombre total de messages que l'utilisateur n'a pas encore lus.

			✔ Paramètres :
			  - matricule (Query)

			🔎 **Tests QA recommandés :**
			- Utilisateur actif → valeur correcte
			- Aucun message non lu → 0
			- Utilisateur inexistant → 0 ou erreur (selon service)

			Exemple :
			```
			GET /api/messages/unread-count?matricule=A100
			```
			""")
	@ApiResponse(responseCode = "200", description = "Nombre des messages non lus")
	@GetMapping("/unread-count")
	public long getUnreadMessagesCount(@RequestParam String matricule) {
		return messageService.countUnreadMessages(matricule);
	}

	// ============================================================
	// 5️⃣ UPLOAD AUDIO
	// ============================================================
	@Operation(summary = "Uploader un fichier audio", description = """
			Upload d'un fichier audio pour un message vocal.

			✔ Accepté : multipart/form-data
			✔ Enregistrement sous `/uploads/audio/`

			🔎 **Tests QA recommandés :**
			- Upload fichier audio valide (mp3, wav)
			- Upload fichier non audio → erreur logique ?
			- Fichier vide → 500 ou 400 selon implémentation
			- Vérifier accessibilité via URL retournée
			- Fichier > 5MB → comportement ?

			Exemple cURL :
			```
			curl -F "file=@voice.mp3" http://localhost:8080/api/messages/upload/audio
			```
			""")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Upload réussi", content = @Content(schema = @Schema(example = "{\"fileUrl\": \"uploads/audio/1700000000_voice.mp3\"}"))),
			@ApiResponse(responseCode = "500", description = "Erreur serveur lors de l’upload") })
	@PostMapping("/upload/audio")
	public ResponseEntity<Map<String, String>> uploadAudio(@RequestParam("file") MultipartFile file) {
		try {
			String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
			Path path = Paths.get(UPLOAD_DIR + fileName);
			Files.createDirectories(path.getParent());
			Files.write(path, file.getBytes());
			return ResponseEntity.ok(Map.of("fileUrl", "uploads/audio/" + fileName));
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Impossible d'uploader le fichier"));
		}
	}
}