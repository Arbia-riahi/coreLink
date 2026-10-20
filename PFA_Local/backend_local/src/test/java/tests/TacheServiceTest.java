package tests;


import com.ProjectManagement.DTO.*;
import com.ProjectManagement.entities.*;
import com.ProjectManagement.services.*;
import com.ProjectManagement.repositories.*;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class TacheServiceTest {

    @Mock
    private TacheRepository tacheRepository;
    @Mock
    private NotificationService notificationService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProjetRepository projetRepository;

    @InjectMocks
    private TacheService tacheService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ✅ TEST 1 : toDTO()
    @Test
    void toDTO_shouldConvertEntityToDTO() {
        User assignee = new User();
        assignee.setMatricule("M123");
        assignee.setNom("Doe");
        assignee.setPrenom("John");
        assignee.setImage("photo.jpg");

        Projet projet = new Projet();
        projet.setNumeroProjet(1L);

        Tache tache = new Tache();
        tache.setId(10L);
        tache.setTitre("Fix bug");
        tache.setDescription("Corriger un bug critique");
        tache.setEtat(E_EtatTache.EN_COURS);
        tache.setPriorite(E_PrioriteTache.HAUTE);
        tache.setDateCreation(LocalDateTime.now().minusDays(1));
        tache.setDateFin(LocalDateTime.now().plusDays(3));
        tache.setAssigneeA(assignee);
        tache.setProjet(projet);
        tache.setAutresAssignes(List.of(assignee));

        TacheDTO dto = tacheService.toDTO(tache);

        assertThat(dto.getTitre()).isEqualTo("Fix bug");
        assertThat(dto.getAssigneeMatricule()).isEqualTo("M123");
        assertThat(dto.getProjetId()).isEqualTo(1L);
        assertThat(dto.getAutresAssignes()).hasSize(1);
    }

    // ✅ TEST 2 : fromDTO() - méthode désormais package-private (plus de Whitebox)
    @Test
    void fromDTO_shouldConvertDTOToEntity() {
        TacheDTO dto = new TacheDTO();
        dto.setTitre("Nouvelle tâche");
        dto.setDescription("Tâche test");
        dto.setEtat("A_FAIRE");
        dto.setPriorite("MOYENNE");
        dto.setDateFin(LocalDateTime.now().plusDays(3));
        dto.setAssigneeMatricule("M001");

        User user = new User();
        user.setMatricule("M001");
        user.setRole(ERole.MEMBER);

        when(userRepository.findByMatricule("M001")).thenReturn(Optional.of(user));

        Tache tache = tacheService.fromDTO(dto);

        assertThat(tache.getAssigneeA()).isEqualTo(user);
        assertThat(tache.getEtat()).isEqualTo(E_EtatTache.A_FAIRE);
        assertThat(tache.getPriorite()).isEqualTo(E_PrioriteTache.MOYENNE);
    }

    // ✅ TEST 3 : createTacheFromDTO() avec notifications
    @Test
    void createTacheFromDTO_shouldSaveAndNotifyAssignee() {
        TacheDTO dto = new TacheDTO();
        dto.setTitre("Implémenter nouvelle feature");
        dto.setEtat("A_FAIRE");
        dto.setPriorite("HAUTE");
        dto.setDateFin(LocalDateTime.now().plusDays(5));
        dto.setAssigneeMatricule("U01");

        User assignee = new User();
        assignee.setMatricule("U01");
        assignee.setRole(ERole.MEMBER);

        Projet projet = new Projet();
        projet.setNumeroProjet(5L);
        projet.setDateFin(LocalDate.now().plusDays(10));

        Tache saved = new Tache();
        saved.setId(99L);
        saved.setTitre(dto.getTitre());
        saved.setAssigneeA(assignee);
        saved.setProjet(projet);

        when(userRepository.findByMatricule("U01")).thenReturn(Optional.of(assignee));
        when(projetRepository.findById(anyLong())).thenReturn(Optional.of(projet));
        when(tacheRepository.save(any(Tache.class))).thenReturn(saved);

        TacheDTO result = tacheService.createTacheFromDTO(dto);

        assertThat(result.getTitre()).isEqualTo(dto.getTitre());
        verify(notificationService, times(1)).creerNotification(
                eq(assignee),
                contains("assigné à la tâche"),
                eq(TypeNotification.TACHE_ASSIGNATION),
                any(),
                any()
        );
    }

    // ✅ TEST 4 : updateEtat()
    @Test
    void updateEtat_shouldChangeEtatAndIncrementModificationCount() {
        Projet projet = new Projet();
        projet.setNumeroProjet(10L);

        Tache tache = new Tache();
        tache.setId(77L);
        tache.setEtat(E_EtatTache.A_FAIRE);
        tache.setNombreModifications(0);
        tache.setProjet(projet);

        when(tacheRepository.findById(77L)).thenReturn(Optional.of(tache));
        when(tacheRepository.save(any(Tache.class))).thenReturn(tache);
        when(projetRepository.findById(10L)).thenReturn(Optional.of(projet));

        tacheService.updateEtat(77L, "TERMINE");

        assertThat(tache.getEtat()).isEqualTo(E_EtatTache.TERMINE);
        assertThat(tache.getNombreModifications()).isEqualTo(1);
        verify(projetRepository).save(any());
    }

    // ✅ TEST 5 : calculerTauxBlocage()
    @Test
    void calculerTauxBlocage_shouldReturnCorrectPercentage() {
        Tache t1 = new Tache(); t1.setEtat(E_EtatTache.BLOQUE);
        Tache t2 = new Tache(); t2.setEtat(E_EtatTache.A_FAIRE);
        Tache t3 = new Tache(); t3.setEtat(E_EtatTache.BLOQUE);

        when(tacheRepository.findByProjet_NumeroProjetAndArchivedFalse(1L))
                .thenReturn(List.of(t1, t2, t3));

        double result = tacheService.calculerTauxBlocage(1L);

        assertThat(result).isEqualTo((2.0 / 3.0) * 100);
    }

    // ✅ TEST 6 : updateProjetStatus()
    @Test
    void updateProjetStatus_shouldSetProjectStatusCorrectly() {
        Projet projet = new Projet();
        projet.setNumeroProjet(1L);

        Tache t1 = new Tache(); t1.setEtat(E_EtatTache.TERMINE);
        Tache t2 = new Tache(); t2.setEtat(E_EtatTache.TERMINE);

        when(projetRepository.findById(1L)).thenReturn(Optional.of(projet));
        when(tacheRepository.findByProjet_NumeroProjetAndArchivedFalse(1L)).thenReturn(List.of(t1, t2));

        tacheService.updateProjetStatus(1L);

        assertThat(projet.getStatus()).isEqualTo(EProjet.TERMINE);
        verify(projetRepository).save(projet);
    }

    // ✅ TEST 7 : getTacheById() Exception
    @Test
    void getTacheById_shouldThrowExceptionIfNotFound() {
        when(tacheRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tacheService.getTacheById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Tâche introuvable");
    }
}
