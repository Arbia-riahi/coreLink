package com.ProjectManagement.websocket;

import com.ProjectManagement.DTO.MemberDTO;
import com.ProjectManagement.repositories.UserRepository;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class WebSocketEventListener {

    private final UserRepository userRepository;
    private final SimpMessageSendingOperations messagingTemplate;

    // Utilisateurs en ligne (matricule)
    private final Set<String> onlineMatricules = ConcurrentHashMap.newKeySet();
    private final Map<String, String> sessionMap = new ConcurrentHashMap<>();

    // Pour éviter d'envoyer la même liste en boucle
    private List<String> lastBroadcastMatricules = new ArrayList<>();

    @Autowired
    public WebSocketEventListener(UserRepository userRepository,
                                  SimpMessageSendingOperations messagingTemplate) {
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String matricule = headerAccessor.getFirstNativeHeader("username");

        if (matricule != null) {
            sessionMap.put(headerAccessor.getSessionId(), matricule);
            onlineMatricules.add(matricule);
            broadcastOnlineUsers();
            System.out.println("✅ " + matricule + " connecté.");
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String matricule = sessionMap.remove(headerAccessor.getSessionId());

        if (matricule != null) {
            onlineMatricules.remove(matricule);
            broadcastOnlineUsers();
            System.out.println("❌ " + matricule + " déconnecté.");
        }
    }

    /**
     * Envoie la liste des utilisateurs en ligne (nom, prenom, image)
     */
    
    @PostConstruct
    public void init() {
        broadcastOnlineUsers();
    }

    private void broadcastOnlineUsers() {
        List<String> currentMatricules = new ArrayList<>(onlineMatricules);
        if (currentMatricules.equals(lastBroadcastMatricules)) return;

        List<MemberDTO> users = currentMatricules.stream()
                .map(matricule -> userRepository.findByMatricule(matricule)
                        .map(user -> new MemberDTO(
                                user.getMatricule(),
                                user.getNom(),
                                user.getPrenom(),
                                user.getImage() != null ? user.getImage() : ""
                        ))
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        messagingTemplate.convertAndSend("/topic/online-users", users);

        lastBroadcastMatricules = currentMatricules;
    }

}


