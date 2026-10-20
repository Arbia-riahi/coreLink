package tests;


import com.ProjectManagement.DTO.CommentaireDTO;
import com.ProjectManagement.entities.*;
import com.ProjectManagement.services.*;
import com.ProjectManagement.repositories.CommentaireRepository;
import com.ProjectManagement.repositories.TacheRepository;
import com.ProjectManagement.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentaireServiceTest {

    @Mock
    private CommentaireRepository commentaireRepository;

    @Mock
    private TacheRepository tacheRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private CommentaireService commentaireService;

    private User auteur;
    private Tache tache;
    private Commentaire commentaire;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        auteur = new User();
        auteur.setMatricule("U123");
        auteur.setNom("Dupont");
        auteur.setPrenom("Jean");
        auteur.setImage("image.png");

        tache = new Tache();
        tache.setId(1L);
        tache.setNombreCommentaires(3);

        commentaire = new Commentaire();
        commentaire.setId(10L);
        commentaire.setContenu("Bonjour !");
        commentaire.setDate(LocalDateTime.now());
        commentaire.setAuteur(auteur);
        commentaire.setTache(tache);
    }

    /** ✅ getCommentairesDTOByTache : succès */
    @Test
    void testGetCommentairesDTOByTache_Success() {
        when(tacheRepository.findById(1L)).thenReturn(Optional.of(tache));
        when(commentaireRepository.findByTacheOrderByDateAsc(tache))
                .thenReturn(List.of(commentaire));

        List<CommentaireDTO> result = commentaireService.getCommentairesDTOByTache(1L);

        assertEquals(1, result.size());
        assertEquals("Dupont", result.get(0).getAuteurNom());
        assertTrue(result.get(0).getAuteurImage().contains("http://localhost:8081/uploads/files/"));
    }

    /** ❌ getCommentairesDTOByTache : tâche inexistante */
    @Test
    void testGetCommentairesDTOByTache_TaskNotFound() {
        when(tacheRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> commentaireService.getCommentairesDTOByTache(1L));
    }

    /** ✅ ajouterCommentaire : succès sans parent */
    @Test
    void testAjouterCommentaire_Success() {
        when(tacheRepository.findById(1L)).thenReturn(Optional.of(tache));
        when(userRepository.findById("U123")).thenReturn(Optional.of(auteur));
        when(commentaireRepository.save(any())).thenAnswer(inv -> {
            Commentaire c = inv.getArgument(0);
            c.setId(99L);
            return c;
        });

        CommentaireDTO dto = commentaireService.ajouterCommentaire(1L, "U123",
                "Un super commentaire", null);

        assertEquals("Dupont", dto.getAuteurNom());
        verify(notificationService).notifierNouveauCommentaire(eq(auteur), eq(tache), anyString());
    }

    /** ✅ ajouterCommentaire : succès avec parent */
    @Test
    void testAjouterCommentaire_WithParent() {
        Commentaire parent = new Commentaire();
        parent.setId(5L);
        parent.setAuteur(new User());
        parent.getAuteur().setNom("Parent");
        parent.getAuteur().setMatricule("P001");

        when(tacheRepository.findById(1L)).thenReturn(Optional.of(tache));
        when(userRepository.findById("U123")).thenReturn(Optional.of(auteur));
        when(commentaireRepository.findById(5L)).thenReturn(Optional.of(parent));
        when(commentaireRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        commentaireService.ajouterCommentaire(1L, "U123", "Merci @Parent Test", 5L);

        verify(notificationService).creerNotification(
                eq(parent.getAuteur()), anyString(),
                eq(TypeNotification.REPONSE_COMMENTAIRE),
                eq(tache.getProjet()), eq(tache)
        );
    }

    /** ❌ ajouterCommentaire : tâche introuvable */
    @Test
    void testAjouterCommentaire_TaskNotFound() {
        when(tacheRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> commentaireService.ajouterCommentaire(99L, "U123", "Contenu", null));
    }

    /** ❌ ajouterCommentaire : utilisateur introuvable */
    @Test
    void testAjouterCommentaire_UserNotFound() {
        when(tacheRepository.findById(1L)).thenReturn(Optional.of(tache));
        when(userRepository.findById("U404")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> commentaireService.ajouterCommentaire(1L, "U404", "Texte", null));
    }

    /** ✅ modifierCommentaire : succès */
    @Test
    void testModifierCommentaire_Success() {
        when(commentaireRepository.findById(10L)).thenReturn(Optional.of(commentaire));
        when(commentaireRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        CommentaireDTO dto = commentaireService.modifierCommentaire(10L, "Texte modifié");

        assertEquals("Texte modifié", dto.getContenu());
        verify(commentaireRepository).save(any(Commentaire.class));
    }

    /** ❌ modifierCommentaire : vide */
    @Test
    void testModifierCommentaire_Vide() {
        assertThrows(IllegalArgumentException.class,
                () -> commentaireService.modifierCommentaire(10L, " "));
    }

    /** ❌ modifierCommentaire : introuvable */
    @Test
    void testModifierCommentaire_NotFound() {
        when(commentaireRepository.findById(10L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> commentaireService.modifierCommentaire(10L, "Texte"));
    }

    /** ✅ supprimerCommentaire : succès */
    @Test
    void testSupprimerCommentaire_Success() {
        commentaire.setTache(tache);
        when(commentaireRepository.findById(10L)).thenReturn(Optional.of(commentaire));

        commentaireService.supprimerCommentaire(10L);

        verify(commentaireRepository).delete(commentaire);
        verify(tacheRepository).save(tache);
        assertTrue(tache.getNombreCommentaires() >= 0);
    }

    /** ❌ supprimerCommentaire : introuvable */
    @Test
    void testSupprimerCommentaire_NotFound() {
        when(commentaireRepository.findById(77L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> commentaireService.supprimerCommentaire(77L));
    }

    /** ✅ convertToDTO : image par défaut */
    @Test
    void testConvertToDTO_DefaultImage() {
        User noImageUser = new User();
        noImageUser.setNom("Doe");
        noImageUser.setPrenom("John");

        Commentaire c = new Commentaire();
        c.setAuteur(noImageUser);
        c.setId(1L);
        c.setContenu("Salut");
        c.setDate(LocalDateTime.now());

        CommentaireDTO dto = invokePrivateConvertToDTO(c);

        assertTrue(dto.getAuteurImage().contains("default-avatar.png"));
    }

    // Méthode d’accès au convertToDTO privé
    private CommentaireDTO invokePrivateConvertToDTO(Commentaire c) {
        try {
            var method = CommentaireService.class.getDeclaredMethod("convertToDTO", Commentaire.class);
            method.setAccessible(true);
            return (CommentaireDTO) method.invoke(commentaireService, c);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

