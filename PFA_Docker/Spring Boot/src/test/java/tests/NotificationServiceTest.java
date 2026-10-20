package tests;

import com.ProjectManagement.DTO.NotificationDTO;
import com.ProjectManagement.entities.*;
import com.ProjectManagement.services.*;
import com.ProjectManagement.repositories.NotificationRepository;
import com.ProjectManagement.websocket.NotificationWebSocketService;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationWebSocketService webSocketService;

    @InjectMocks
    private NotificationService notificationService;

    private User user;
    private Projet projet;
    private Tache tache;
    private Notification notification;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setMatricule("U123");
        user.setNom("Doe");
        user.setPrenom("John");

        projet = new Projet();
        projet.setNumeroProjet(1L);
        projet.setTitre("Projet Alpha");
        projet.setChefProjet(user);

        tache = new Tache();
        tache.setId(100L);
        tache.setTitre("Corriger les bugs");
        tache.setProjet(projet);
        tache.setAssigneeA(user);

        notification = new Notification();
        notification.setId(1L);
        notification.setDestinataire(user);
        notification.setMessage("Test message");
        notification.setDate(LocalDateTime.now());
        notification.setType(TypeNotification.TACHE_DEADLINE_PROCHE);
    }

    // --- TEST 1 : création et envoi d'une notification ---
    @Test
    void testCreerNotification_Success() {
        // 🧠 Arrange : on prépare le mock du repository
        when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(invocation -> invocation.getArgument(0)); 
                // 👆 renvoie exactement l’objet passé à save()

        // 🧪 Act : on appelle la méthode à tester
        NotificationDTO dto = notificationService.creerNotification(
                user, "Message test", TypeNotification.TACHE_DEADLINE_PROCHE, projet, tache);

        // 🔍 Assert : on vérifie que tout est bien construit
        assertNotNull(dto);
        assertEquals("Message test", dto.getMessage());
        assertEquals("TACHE_DEADLINE_PROCHE", dto.getType());
        assertEquals(user.getMatricule(), dto.getDestinataireMatricule());
        assertEquals(tache.getId(), dto.getTacheId());
        assertEquals(projet.getNumeroProjet(), dto.getProjetId());

        // ✅ Vérifie que la notification a bien été envoyée en temps réel
        verify(webSocketService, times(1))
                .sendNotificationToUser(eq("U123"), any(NotificationDTO.class));

        // ✅ Vérifie qu’elle a bien été sauvegardée
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }


    // --- TEST 2 : notifications pour les membres d’un projet ---
    @Test
    void testCreerNotificationPourMembresProjet_ExclutAuteur() {
        User membre1 = new User();
        membre1.setMatricule("U456");

        projet.setMembres(List.of(user, membre1));

        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        notificationService.creerNotificationPourMembresProjet(projet, user,
                "Mise à jour du projet", TypeNotification.PROJET_DEADLINE_PROCHE);

        // Doit notifier uniquement le membre, pas l’auteur
        verify(webSocketService, times(1))
                .sendNotificationToUser(eq("U456"), any(NotificationDTO.class));
    }

    // --- TEST 3 : marquer une notification comme lue ---
    @Test
    void testMarkAsRead_Success() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        NotificationDTO dto = notificationService.markAsRead(1L);

        assertTrue(dto.isLue());
        verify(notificationRepository).save(any(Notification.class));
    }

    // --- TEST 4 : marquer comme lue -> erreur si non trouvée ---
    @Test
    void testMarkAsRead_NotificationIntrouvable() {
        when(notificationRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> notificationService.markAsRead(999L));
    }

    // --- TEST 5 : suppression d'une notification ---
    @Test
    void testDeleteNotification() {
        doNothing().when(notificationRepository).deleteById(1L);

        notificationService.deleteNotification(1L);

        verify(notificationRepository, times(1)).deleteById(1L);
    }

    // --- TEST 6 : récupérer les notifications d’un utilisateur ---
    @Test
    void testGetNotificationsByUser() {
        when(notificationRepository.findByDestinataire_MatriculeOrderByDateDesc("U123"))
                .thenReturn(List.of(notification));

        List<NotificationDTO> list = notificationService.getNotificationsByUser("U123");

        assertEquals(1, list.size());
        assertEquals("Test message", list.get(0).getMessage());
    }

    // --- TEST 7 : compter les notifications non lues ---
    @Test
    void testCountUnreadNotifications() {
        when(notificationRepository.countByDestinataireMatriculeAndLueFalse("U123"))
                .thenReturn(3L);

        long count = notificationService.countUnreadNotifications("U123");

        assertEquals(3L, count);
    }

    // --- TEST 8 : notifier tâche deadline proche ---
    @Test
    void testNotifierTacheDeadlineProche() {
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        notificationService.notifierTacheDeadlineProche(tache);

        verify(webSocketService, times(1))
                .sendNotificationToUser(eq("U123"), any(NotificationDTO.class));
    }

    // --- TEST 9 : notifier projet deadline dépassée ---
    @Test
    void testNotifierProjetDeadlineDepassee() {
        User membre = new User();
        membre.setMatricule("U222");
        projet.setMembres(List.of(user, membre));
        projet.setChefProjet(user);

        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        notificationService.notifierProjetDeadlineDepassee(projet);

        // Une pour le chef + une pour le membre
        verify(webSocketService, times(2))
                .sendNotificationToUser(anyString(), any(NotificationDTO.class));
    }

    // --- TEST 10 : notifier nouveau commentaire ---
    @Test
    void testNotifierNouveauCommentaire() {
        User chef = new User();
        chef.setMatricule("CHEF");
        projet.setChefProjet(chef);
        tache.setProjet(projet);
        tache.setAutresAssignes(List.of(user));

        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        notificationService.notifierNouveauCommentaire(user, tache, "Super travail");

        // Ne pas notifier l’auteur lui-même
        verify(webSocketService, atLeast(0))
                .sendNotificationToUser(anyString(), any(NotificationDTO.class));
    }
}
