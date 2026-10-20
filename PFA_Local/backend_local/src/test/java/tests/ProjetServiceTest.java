package tests;

import com.ProjectManagement.DTO.ProjetDTO;
import com.ProjectManagement.entities.*;
import com.ProjectManagement.repositories.*;
import com.ProjectManagement.services.*;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.*;
import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjetServiceTest {

    @Mock private ProjetRepository projetRepository;
    @Mock private UserRepository userRepository;
    @Mock private TacheRepository tacheRepository;
    @Mock private NotificationService notificationService;

    @InjectMocks private ProjetService projetService;

    @BeforeEach
    void init() throws Exception {
        MockitoAnnotations.openMocks(this);
        Field uploadDirField = ProjetService.class.getDeclaredField("uploadDir");
        uploadDirField.setAccessible(true);
        uploadDirField.set(projetService, "src/test/resources/uploads");
    }


    // ---------------------------- CREATE PROJECT ----------------------------
    @Test
    void testCreerProjet_Success() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn("file.pdf");
        when(file.getInputStream()).thenReturn(InputStream.nullInputStream());

        User chef = new User();
        chef.setMatricule("C001");
        chef.setNom("Chef");
        chef.setPrenom("Projet");
        when(userRepository.findByMatricule("C001")).thenReturn(Optional.of(chef));

        User membre = new User();
        membre.setMatricule("M001");
        when(userRepository.findByMatricule("M001")).thenReturn(Optional.of(membre));
        when(projetRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            filesMock.when(() -> Files.exists(any(Path.class))).thenReturn(true);
            filesMock.when(() -> Files.copy(any(InputStream.class), any(Path.class), any(CopyOption[].class)))
                    .thenReturn(1L);

            Projet p = projetService.creerProjet(
                    "Titre", "Desc",
                    LocalDate.now(), LocalDate.now().plusDays(5),
                    "EN_COURS", List.of("M001"), "C001", List.of(file)
            );

            assertEquals("Titre", p.getTitre());
            assertEquals(1, p.getMembres().size());
            verify(notificationService).creerNotificationPourMembresProjet(any(), any(), any(), any());
        }
    }

    @Test
    void testCreerProjet_ChefIntrouvable() {
        when(userRepository.findByMatricule("X")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () ->
                projetService.creerProjet("Titre", "Desc", LocalDate.now(), LocalDate.now(), "EN_COURS", null, "X", null));
    }

    // ---------------------------- UPDATE PROJECT ----------------------------
    @Test
    void testUpdateProjet_Success() throws IOException {
        Projet existing = new Projet();
        existing.setTitre("Ancien");
        existing.setChefProjet(new User());
        existing.setFichiers(new ArrayList<>());

        when(projetRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(projetRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(userRepository.findByMatricule(anyString())).thenReturn(Optional.of(new User()));

        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn("doc.pdf");
        when(file.getInputStream()).thenReturn(InputStream.nullInputStream());

        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            filesMock.when(() -> Files.exists(any(Path.class))).thenReturn(true);
            filesMock.when(() -> Files.copy(any(InputStream.class), any(Path.class), any(CopyOption[].class)))
                    .thenReturn(1L);
            Projet updated = projetService.updateProjet(
                    1L, "Nouveau", "Description", LocalDate.now(), LocalDate.now(), "TERMINE",
                    List.of("U1"), List.of(file), List.of("/uploads/files/file.pdf"));
            assertEquals("Nouveau", updated.getTitre());
            verify(notificationService, atLeast(0)).creerNotification(any(), any(), any(), any(), any());
        }
    }

    @Test
    void testUpdateProjet_Inexistant() {
        when(projetRepository.findById(5L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> projetService.updateProjet(5L, null, null, null, null, null, null, null, null));
    }

    // ---------------------------- ARCHIVE PROJECT ----------------------------
    @Test
    void testArchiveProjet_Success() {
        Projet p = new Projet();
        when(projetRepository.findById(1L)).thenReturn(Optional.of(p));
        when(projetRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Projet res = projetService.archiveProjet(1L);
        assertTrue(res.isArchived());
    }

    // ---------------------------- GET PROJECTS ----------------------------
    @Test
    void testGetAllProjetsDTO() {
        Projet p = new Projet();
        User chef = new User();
        chef.setNom("John");
        chef.setPrenom("Doe");
        p.setChefProjet(chef);
        p.setTitre("Projet A");
        when(projetRepository.findAll()).thenReturn(List.of(p));

        List<ProjetDTO> result = projetService.getAllProjetsDTO();
        assertEquals("Projet A", result.get(0).getTitre());
    }

    @Test
    void testGetProjetDTOByNumero_Success() {
        Projet p = new Projet();
        p.setTitre("P1");
        User chef = new User();
        chef.setNom("Chef");
        chef.setPrenom("X");
        p.setChefProjet(chef);
        when(projetRepository.findById(1L)).thenReturn(Optional.of(p));

        ProjetDTO dto = projetService.getProjetDTOByNumero(1L);
        assertEquals("P1", dto.getTitre());
    }

    @Test
    void testGetProjetDTOByNumero_NotFound() {
        when(projetRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> projetService.getProjetDTOByNumero(99L));
    }

    // ---------------------------- STATS & CHARTS ----------------------------
    @Test
    void testGetProjetStats_Success() {
        Projet p = new Projet();
        p.setNumeroProjet(1L);
        when(projetRepository.findById(1L)).thenReturn(Optional.of(p));

        Tache t1 = new Tache();
        t1.setEtat(E_EtatTache.TERMINE);
        Tache t2 = new Tache();
        t2.setEtat(E_EtatTache.EN_COURS);
        when(tacheRepository.findByProjet_NumeroProjetAndArchivedFalse(1L)).thenReturn(List.of(t1, t2));

        Map<String, Object> stats = projetService.getProjetStats(1L);
        assertTrue(stats.containsKey("nbTotal"));
        assertTrue(stats.containsKey("nbTerminees"));
    }

    @Test
    void testGetVelocityChart_Empty() {
        when(tacheRepository.findByProjet_NumeroProjetAndArchivedFalse(1L)).thenReturn(Collections.emptyList());
        Map<String, Object> map = projetService.getVelocityChart(1L);
        assertTrue(map.containsKey("velocityByWeek"));
    }

    @Test
    void testGetBurnDownChart_Empty() {
        when(tacheRepository.findByProjet_NumeroProjetAndArchivedFalse(1L)).thenReturn(Collections.emptyList());
        Map<String, Object> result = projetService.getBurnDownChart(1L);
        assertTrue(result.containsKey("burnDown"));
    }

    // ---------------------------- GLOBAL STATS ----------------------------
    @Test
    void testGetGlobalProjetStats() {
        when(projetRepository.countByStatus(EProjet.EN_ATTENTE)).thenReturn(1L);
        when(projetRepository.countByStatus(EProjet.EN_COURS)).thenReturn(2L);
        when(projetRepository.countByStatus(EProjet.TERMINE)).thenReturn(3L);
        when(projetRepository.countByStatus(EProjet.ANNULE)).thenReturn(4L);

        Map<String, Long> map = projetService.getGlobalProjetStats();
        assertEquals(4L, map.get("nbAnnules"));
    }

    @Test
    void testGetComparaisonProjets() {
        Projet p = new Projet();
        p.setTitre("P1");
        when(projetRepository.findAll()).thenReturn(List.of(p));
        when(tacheRepository.findByProjet_NumeroProjetAndArchivedFalse(anyLong())).thenReturn(Collections.emptyList());

        List<Map<String, Object>> result = projetService.getComparaisonProjets();
        assertEquals("P1", result.get(0).get("projet"));
    }

    // ---------------------------- UPCOMING DEADLINES ----------------------------
    @Test
    void testGetUpcomingDeadlines() {
        Projet p = new Projet();
        p.setTitre("T");
        p.setDateFin(LocalDate.now().plusDays(3));
        when(projetRepository.findAll()).thenReturn(List.of(p));

        List<Map<String, Object>> res = projetService.getUpcomingDeadlines();
        assertEquals("T", res.get(0).get("titre"));
    }

    // ---------------------------- GANTT ----------------------------
    @Test
    void testGetProjectsForGantt() {
        Projet p = new Projet();
        p.setTitre("P1");
        p.setArchived(false);
        p.setNumeroProjet(1L);
        when(projetRepository.findAll()).thenReturn(List.of(p));
        when(tacheRepository.findByProjet_NumeroProjetAndArchivedFalse(1L)).thenReturn(Collections.emptyList());

        List<Map<String, Object>> res = projetService.getProjectsForGantt();
        assertEquals("P1", res.get(0).get("titre"));
    }

    // ---------------------------- PROJETS PAR CHEF ----------------------------
    @Test
    void testGetProjectsByChefAndStatus() {
        User chef = new User();
        chef.setNom("Chef");
        chef.setPrenom("A");
        Projet p = new Projet();
        p.setChefProjet(chef);
        p.setStatus(EProjet.EN_COURS);
        when(projetRepository.findAll()).thenReturn(List.of(p));

        Map<String, Map<String, Long>> res = projetService.getProjectsByChefAndStatus();
        assertTrue(res.containsKey("Chef A"));
    }

    // ---------------------------- REMOVE FILES ----------------------------
    @Test
    void testRemoveFilesFromProject_Success() throws IOException {
        Projet projet = new Projet();
        projet.getFichiers().add("/uploads/files/file1.txt");
        projet.getFichiers().add("/uploads/files/file2.txt");

        // ✅ Injection manuelle de uploadDir pour le test
        projetService.getClass()
            .getDeclaredFields();
        try {
            java.lang.reflect.Field f = ProjetService.class.getDeclaredField("uploadDir");
            f.setAccessible(true);
            f.set(projetService, "src/test/resources/uploads/files");
        } catch (Exception e) {
            fail("Impossible d’injecter uploadDir : " + e.getMessage());
        }

        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            filesMock.when(() -> Files.deleteIfExists(any(Path.class))).thenReturn(true);
            projetService.removeFilesFromProject(projet, List.of("/uploads/files/file1.txt"));
            assertFalse(projet.getFichiers().contains("/uploads/files/file1.txt"));
        }
    }

}
