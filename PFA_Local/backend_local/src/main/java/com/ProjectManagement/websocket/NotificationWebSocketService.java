package com.ProjectManagement.websocket;

import com.ProjectManagement.DTO.NotificationDTO;
import com.ProjectManagement.entities.Notification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class NotificationWebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationWebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendNotificationToUser(String matricule, NotificationDTO dto) {
        System.out.println("📡 [WS] Envoi à /user/" + matricule + "/queue/notifications : " + dto.getMessage());
        messagingTemplate.convertAndSendToUser(matricule, "/queue/notifications", dto);
    }
}

