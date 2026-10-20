package com.ProjectManagement.repositories;

import com.ProjectManagement.entities.Message;
import com.ProjectManagement.entities.Reaction;
import com.ProjectManagement.entities.ReactionType;
import com.ProjectManagement.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    
    // Récupérer toutes les réactions d'un message
    List<Reaction> findByMessageId(Long messageId);

    // Vérifier si un utilisateur a déjà réagi à un message
    Optional<Reaction> findByMessageAndUser(Message message, User user);

    // Récupérer les réactions d'un utilisateur sur un message
    List<Reaction> findByUserAndMessage(User user, Message message);
    Optional<Reaction> findByMessageAndUserAndType(Message message, User user, ReactionType type);
    

	
}
