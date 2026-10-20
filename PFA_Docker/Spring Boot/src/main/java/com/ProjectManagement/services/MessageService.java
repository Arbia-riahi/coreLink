package com.ProjectManagement.services;

import com.ProjectManagement.DTO.*;

import com.ProjectManagement.entities.*;

import com.ProjectManagement.repositories.*;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;

@Service
public class MessageService {

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ReactionRepository reactionRepository;

	/**
	 * Sauvegarde un message texte ou multimédia.
	 */

	public MessageDTO saveMessage(String matricule, String content, String fileUrl, String audioUrl) {
		User sender = userRepository.findByMatricule(matricule)
				.orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

		Message message = new Message(sender, content, fileUrl);
		message.setAudioUrl(audioUrl);
		message.setTimestamp(LocalDateTime.now());

		// Détection des mentions
		Set<User> mentionedUsers = extractMentionedUsers(content);
		message.setMentionedUsers(mentionedUsers);

		messageRepository.save(message);

		// ❌ Ne plus notifier les utilisateurs mentionnés
		// notifyMentionedUsers(mentionedUsers, message);

		return convertToDTO(message);
	}

	/**
	 * Récupère tous les messages (texte et fichiers).
	 */
	public List<MessageDTO> getAllMessages() {
		return messageRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	/**
	 * Marque un message comme lu par un utilisateur.
	 */
	@Transactional
	public void markMessageAsRead(Long messageId, String readerMatricule) {
		Message message = messageRepository.findById(messageId)
				.orElseThrow(() -> new RuntimeException("Message introuvable"));
		User reader = userRepository.findByMatricule(readerMatricule)
				.orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

		if (!message.getSeenBy().contains(reader)) {
			message.getSeenBy().add(reader);
			messageRepository.save(message);

			// Récupérer la liste mise à jour
			List<MemberDTO> seenBy = message.getSeenBy().stream()
					.map(user -> new MemberDTO(user.getMatricule(), user.getNom(), user.getPrenom(), user.getImage()))
					.toList();

			// Publier sur WebSocket
			messagingTemplate.convertAndSend("/topic/message-seen",
					Map.of("messageId", message.getId(), "seenBy", seenBy));
		}
		messageRepository.saveAndFlush(message);

	}

	/**
	 * Retourne la liste des utilisateurs qui ont vu un message.
	 */
	public List<MemberDTO> getSeenBy(Long messageId) {
		Message message = messageRepository.findById(messageId)
				.orElseThrow(() -> new RuntimeException("Message introuvable"));

		return message.getSeenBy().stream()
				.map(user -> new MemberDTO(user.getMatricule(), user.getNom(), user.getPrenom(), user.getImage()))
				.collect(Collectors.toList());
	}

	private MessageDTO convertToDTO(Message message) {
		// Conversion des utilisateurs ayant vu le message
		List<MemberDTO> seenByList = message.getSeenBy().stream()
				.map(user -> new MemberDTO(user.getMatricule(), user.getNom(), user.getPrenom(), user.getImage()))
				.toList();

		// Conversion des réactions en ReactionDTO
		List<ReactionDTO> reactionsList = message.getReactions().stream().map(
				r -> new ReactionDTO(message.getId(), r.getType().getEmoji(), new MemberDTO(r.getUser().getMatricule(),
						r.getUser().getNom(), r.getUser().getPrenom(), r.getUser().getImage())))
				.toList();

		// Retourne le MessageDTO
		MessageDTO dto = new MessageDTO(message.getId(),
				new MemberDTO(message.getSender().getMatricule(), message.getSender().getNom(),
						message.getSender().getPrenom(), message.getSender().getImage()),
				message.getContent(), message.getFileUrl(), message.getAudioUrl(), message.getTimestamp(), seenByList);

		// Ajout des réactions au DTO
		dto.setReactions(reactionsList);
		dto.setMentions(message.getMentionedUsers().stream()
				.map(user -> new MemberDTO(user.getMatricule(), user.getNom(), user.getPrenom(), user.getImage()))
				.toList());

		return dto;
	}

	public long countUnreadMessages(String matricule) {
		User user = userRepository.findByMatricule(matricule)
				.orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
		return messageRepository.countBySeenByNotContainingAndSenderNot(user, user);
	}

	@Transactional
	public ReactionDTO addReaction(Long messageId, String emoji, String userMatricule) {
		Message message = messageRepository.findById(messageId)
				.orElseThrow(() -> new RuntimeException("Message non trouvé"));

		User user = userRepository.findByMatricule(userMatricule)
				.orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

		ReactionType reactionType = ReactionType.fromEmoji(emoji);

		Optional<Reaction> existingReaction = reactionRepository.findByMessageAndUserAndType(message, user,
				reactionType);

		ReactionDTO reactionDTO;

		if (existingReaction.isPresent()) {
			reactionRepository.delete(existingReaction.get());
			reactionDTO = new ReactionDTO(messageId, emoji,
					new MemberDTO(user.getMatricule(), user.getNom(), user.getPrenom(), user.getImage()), true);
		} else {
			Reaction reaction = new Reaction(reactionType, user, message);
			reactionRepository.save(reaction);
			message.getReactions().add(reaction);
			reactionDTO = new ReactionDTO(messageId, emoji,
					new MemberDTO(user.getMatricule(), user.getNom(), user.getPrenom(), user.getImage()), false);
		}

		// 🔥 Diffusion en temps réel aux autres utilisateurs
		messagingTemplate.convertAndSend("/topic/reactions", reactionDTO);

		return reactionDTO;
	}

	private Set<User> extractMentionedUsers(String content) {
		Set<User> mentioned = new HashSet<>();

		if (content == null)
			return mentioned;

		Pattern pattern = Pattern.compile("@(\\w+)\\s+(\\w+)");
		Matcher matcher = pattern.matcher(content);

		while (matcher.find()) {
			String nom = matcher.group(1);
			String prenom = matcher.group(2);
			userRepository.findByNomAndPrenom(nom, prenom).ifPresent(mentioned::add);
		}

		return mentioned;
	}

}
