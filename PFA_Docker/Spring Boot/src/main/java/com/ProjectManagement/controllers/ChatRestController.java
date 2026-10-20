package com.ProjectManagement.controllers;

import com.ProjectManagement.DTO.*;
import com.ProjectManagement.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Tag(name = "Chat & Messaging", description = """
		Endpoints REST & WebSocket dédiés au chat en temps réel.

		**À destination des QA Automation :**

		✔ Tests WebSocket (STOMP)
		✔ Tests d’envoi de message en temps réel
		✔ Tests upload fichiers (images + audio)
		✔ Tests réactions (REST + WebSocket)
		✔ Validation synchronisation FE/BE
		✔ Vérification du broadcast des utilisateurs connectés

		⚠ Note : Swagger ne documente pas les WebSockets automatiquement.
		Les méthodes @MessageMapping sont décrites dans la documentation ci-dessous.
		""")
@Controller
public class ChatRestController {

	private final Set<String> onlineUsers = ConcurrentHashMap.newKeySet();

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private MessageService messageService;

	private static final String UPLOAD_DIR = "/app/uploads";

	/** Envoi de message */
	@MessageMapping("/sendMessage")
	public void sendMessage(MessageDTO messageDTO) {
		if (messageDTO.getSender() != null) {
			String image = (messageDTO.getSender().getImage() != null && !messageDTO.getSender().getImage().isEmpty())
					? messageDTO.getSender().getImage()
					: "/uploads/default.png";
			messageDTO.getSender().setImage(image);
		}

		MessageDTO savedMessage = messageService.saveMessage(messageDTO.getSender().getMatricule(),
				messageDTO.getContent(), messageDTO.getFileUrl(), messageDTO.getAudioUrl());

		messagingTemplate.convertAndSend("/topic/messages", savedMessage);
	}

	/** Gestion des utilisateurs connectés */
	@MessageMapping("/userConnected")
	public void userConnected(String username) {
		onlineUsers.add(username);
		broadcastOnlineUsers();
	}

	@MessageMapping("/userDisconnected")
	public void userDisconnected(String username) {
		onlineUsers.remove(username);
		broadcastOnlineUsers();
	}

	private void broadcastOnlineUsers() {
		messagingTemplate.convertAndSend("/topic/online-users", onlineUsers);
	}

	@Operation(summary = "Uploader un fichier (image/document)", description = """
			Permet d’uploader un fichier pour le chat (image, PDF, etc.).

			✔ **Type : multipart/form-data**
			✔ Retourne l’URL publique du fichier sauvegardé.

			**Tests QA recommandés :**
			- Upload image valide
			- Upload fichier non image
			- Fichier vide → 400
			- Vérifier emplacement : /uploads/files/
			- Vérifier que l’URL retournée est accessible en navigateur
			""")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Fichier uploadé avec succès", content = @Content(schema = @Schema(example = "{\"fileUrl\":\"uploads/files/1700000000_image.png\"}"))),
			@ApiResponse(responseCode = "400", description = "Aucun fichier envoyé") })
	@PostMapping("/api/upload")
	public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
		return saveFile(file, "files");
	}

	@Operation(summary = "Uploader un fichier audio", description = """
			Permet d’uploader un fichier audio pour le chat (mp3, wav, etc.).

			✔ **Type : multipart/form-data**
			✔ Le fichier est stocké dans `/uploads/audio/`

			**Tests QA recommandés :**
			- Upload audio valide
			- Upload fichier non audio
			- Tester fichiers > 5MB
			- Vérifier accessibilité de l’URL retournée
			""")
	@PostMapping("/api/upload/audio")
	public ResponseEntity<Map<String, String>> uploadAudio(@RequestParam("file") MultipartFile file)
			throws IOException {
		return saveFile(file, "audio");
	}

	private ResponseEntity<Map<String, String>> saveFile(MultipartFile file, String subDir) throws IOException {
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("fileUrl", ""));
		}

		Path uploadPath = Paths.get(UPLOAD_DIR, subDir);
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
		Path filePath = uploadPath.resolve(fileName);
		Files.write(filePath, file.getBytes());

		return ResponseEntity.ok(Map.of("fileUrl", "uploads/" + subDir + "/" + fileName));
	}

	@Operation(summary = "Ajouter ou supprimer une réaction à un message", description = """
			Ajoute une réaction (emoji) à un message, ou la supprime si elle existe déjà.

			✔ **Type : JSON (REST)**
			✔ En parallèle : broadcast WebSocket → `/topic/reactions`

			🔎 **Tests QA recommandés :**
			- Ajouter réaction (😀)
			- Supprimer réaction existante
			- Tester reaction sur message inexistant
			- Vérifier broadcast temps réel côté WebSocket
			- Tester multi-utilisateurs simultanés

			Exemple JSON :
			```
			{
			  "emoji": "😀",
			  "user": { "matricule": "A100" }
			}
			```
			""")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Réaction ajoutée ou supprimée") })
	@PostMapping("/api/messages/{messageId}/reactions")
	public ResponseEntity<?> addReaction(@PathVariable Long messageId, @RequestBody ReactionDTO reactionDTO) {

		ReactionDTO savedReaction = messageService.addReaction(messageId, reactionDTO.getEmoji(),
				reactionDTO.getUser().getMatricule());

		if (savedReaction == null) {
			return ResponseEntity.ok(Map.of("removed", true));
		}

		messagingTemplate.convertAndSend("/topic/reactions", savedReaction);
		return ResponseEntity.ok(savedReaction);
	}

	/** Ajout d'une réaction via WebSocket (temps réel) */
	@MessageMapping("/addReaction")
	public void handleReaction(ReactionDTO reactionDTO) {
		ReactionDTO updatedReaction = messageService.addReaction(reactionDTO.getMessageId(), reactionDTO.getEmoji(),
				reactionDTO.getUser().getMatricule());

		messagingTemplate.convertAndSend("/topic/reactions", updatedReaction);
	}

}