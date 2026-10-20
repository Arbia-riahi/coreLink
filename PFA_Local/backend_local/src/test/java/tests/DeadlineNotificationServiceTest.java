package tests;


import com.ProjectManagement.entities.*;
import com.ProjectManagement.services.*;
import com.ProjectManagement.repositories.ProjetRepository;
import com.ProjectManagement.repositories.TacheRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.*;
import java.util.*;
import static org.mockito.Mockito.*;


class DeadlineNotificationServiceTest {

    @Mock
    private ProjetRepository projetRepository;

    @Mock
    private TacheRepository tacheRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private DeadlineNotificationService deadlineNotificationService;

    private Projet projetProche;
    private Projet projetDepasse;
    private Tache tacheProche;
    private Tache tacheDepassee;
    private User chef;
    private User membre;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        chef = new User();
        chef.setMatricule("C001");

        membre = new User();
        membre.setMatricule("M001");

        // --- Projets ---
        projetProche = new Projet();
        projetProche.setTitre("Projet proche");
        projetProche.setDateFin(LocalDate.now().plusDays(1));
        projetProche.setChefProjet(chef);
        projetProche.setMembres(List.of(chef, membre));
        projetProche.setArchived(false);

        projetDepasse = new Projet();
        projetDepasse.setTitre("Projet dépassé");
        projetDepasse.setDateFin(LocalDate.now().minusDays(1));
        projetDepasse.setChefProjet(chef);
        projetDepasse.setMembres(List.of(chef, membre));
        projetDepasse.setArchived(false);

        // --- Tâches ---
        tacheProche = new Tache();
        tacheProche.setTitre("Tâche proche");
        tacheProche.setDateFin(LocalDateTime.now().plusHours(10));
        tacheProche.setProjet(projetProche);
        tacheProche.setArchived(false);
        tacheProche.setAssigneeA(chef);
        tacheProche.setAutresAssignes(List.of(membre));

        tacheDepassee = new Tache();
        tacheDepassee.setTitre("Tâche dépassée");
        tacheDepassee.setDateFin(LocalDateTime.now().minusHours(3));
        tacheDepassee.setProjet(projetDepasse);
        tacheDepassee.setArchived(false);
        tacheDepassee.setAssigneeA(chef);
        tacheDepassee.setAutresAssignes(List.of(membre));
    }

    @Test
    void testCheckDeadlines_ProjetProcheEtDepasse() {
        when(projetRepository.findAllWithMembresEtChef()).thenReturn(List.of(projetProche, projetDepasse));
        when(tacheRepository.findAll()).thenReturn(Collections.emptyList());

        deadlineNotificationService.checkDeadlines();

        // Vérifie que les notifications ont été envoyées
        verify(notificationService, atLeastOnce()).creerNotification(
                eq(chef),
                contains("projet"),
                any(),
                any(),
                isNull()
        );

        verify(notificationService, atLeastOnce()).creerNotification(
                eq(membre),
                contains("projet"),
                any(),
                any(),
                isNull()
        );

        // Vérifie que les projets ont été sauvegardés avec flags
        verify(projetRepository, times(2)).save(any(Projet.class));
    }

    @Test
    void testCheckDeadlines_TacheProcheEtDepassee() {
        when(projetRepository.findAllWithMembresEtChef()).thenReturn(Collections.emptyList());
        when(tacheRepository.findAll()).thenReturn(List.of(tacheProche, tacheDepassee));

        deadlineNotificationService.checkDeadlines();

        verify(notificationService, atLeast(2)).creerNotification(
                any(User.class),
                contains("tâche"),
                any(),
                any(),
                any()
        );

        verify(tacheRepository, times(2)).save(any(Tache.class));
    }

    @Test
    void testNotifierNouveauCommentaire() {
        deadlineNotificationService.notifierNouveauCommentaire(projetProche, chef, "Test comment");
        verify(notificationService, atLeastOnce()).creerNotification(
                any(User.class),
                contains("Nouveau commentaire"),
                eq(TypeNotification.NOUVEAU_COMMENTAIRE),
                eq(projetProche),
                isNull()
        );
    }

    @Test
    void testNotifierReponseCommentaire() {
        deadlineNotificationService.notifierReponseCommentaire(projetProche, chef, "Réponse test");
        verify(notificationService, atLeastOnce()).creerNotification(
                any(User.class),
                contains("Réponse à un commentaire"),
                eq(TypeNotification.REPONSE_COMMENTAIRE),
                eq(projetProche),
                isNull()
        );
    }

    @Test
    void testNotifierMentionDansCommentaire() {
        deadlineNotificationService.notifierMentionDansCommentaire(projetProche, membre, "Message mention");
        verify(notificationService, times(1)).creerNotification(
                eq(membre),
                contains("mentionné"),
                eq(TypeNotification.MENTION_DANS_COMMENTAIRE),
                eq(projetProche),
                isNull()
        );
    }
}
