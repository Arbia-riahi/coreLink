package tests;


import com.ProjectManagement.entities.Actualite;
import com.ProjectManagement.repositories.ActualiteRepository;
import com.ProjectManagement.services.ActualiteService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ActualiteServiceTest {

    @Mock
    private ActualiteRepository actualiteRepository;

    @InjectMocks
    private ActualiteService actualiteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /** ✅ Cas nominal : findAll */
    @Test
    void testFindAll_ShouldReturnListWithUrls() {
        List<Actualite> list = List.of(
                new Actualite("Titre 1", "Desc 1", List.of("photo1.png")),
                new Actualite("Titre 2", "Desc 2", List.of("photo2.png"))
        );

        when(actualiteRepository.findAll()).thenReturn(list);

        List<Actualite> result = actualiteService.findAll();

        assertEquals(2, result.size());
        assertTrue(result.get(0).getPhotos().get(0)
                .startsWith("/uploads/files/"));
    }

    /** ✅ findById : succès */
    @Test
    void testFindById_ShouldReturnActualite() {
        Actualite act = new Actualite("Titre", "Desc", List.of("img.png"));
        when(actualiteRepository.findById(1L)).thenReturn(Optional.of(act));

        Actualite result = actualiteService.findById(1L);
        assertEquals("Titre", result.getTitre());
    }

    /** ❌ findById : actualité inexistante */
    @Test
    void testFindById_ShouldThrow_WhenNotFound() {
        when(actualiteRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> actualiteService.findById(1L));
    }

    /** ✅ create : succès */
    @Test
    void testCreate_ShouldSaveActualite() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getContentType()).thenReturn("image/png");
        when(mockFile.getOriginalFilename()).thenReturn("test.png");
        when(mockFile.getInputStream()).thenReturn(InputStream.nullInputStream());

        List<MultipartFile> files = List.of(mockFile);
        Actualite saved = new Actualite("Titre", "Desc", List.of("img.png"));
        when(actualiteRepository.save(any())).thenReturn(saved);

        // Mock Files.copy pour éviter d'écrire réellement sur le disque
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles
                .when(() -> Files.copy(
                        any(InputStream.class),
                        any(Path.class),
                        any(CopyOption[].class)))
                .thenReturn(1L); // ✅ renvoie bien un long

            Actualite result = actualiteService.create(files, "Titre", "Desc");

            assertNotNull(result);
            verify(actualiteRepository, times(1)).save(any(Actualite.class));
        }
    }

    /** ❌ create : liste vide => erreur */
    @Test
    void testCreate_ShouldThrow_WhenNoFiles() {
        assertThrows(IllegalArgumentException.class,
                () -> actualiteService.create(Collections.emptyList(), "Titre", "Desc"));
    }

    /** ❌ create : mauvais type MIME => erreur */
    @Test
    void testCreate_ShouldThrow_WhenInvalidFileType() {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getContentType()).thenReturn("application/pdf");

        List<MultipartFile> files = List.of(mockFile);

        assertThrows(IllegalArgumentException.class,
                () -> actualiteService.create(files, "Titre", "Desc"));
    }

    /** ✅ update : succès */
    @Test
    void testUpdate_ShouldModifyExistingActualite() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getContentType()).thenReturn("image/png");
        when(mockFile.getOriginalFilename()).thenReturn("update.png");
        when(mockFile.getInputStream()).thenReturn(InputStream.nullInputStream());

        Actualite existing = new Actualite("Ancien titre", "Ancienne desc", new ArrayList<>());
        when(actualiteRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(actualiteRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles
                .when(() -> Files.copy(
                        any(InputStream.class),
                        any(Path.class),
                        any(CopyOption[].class)))
                .thenReturn(1L);

            Actualite result = actualiteService.update(1L, "Nouveau", "Desc", List.of(mockFile));

            assertEquals("Nouveau", result.getTitre());
            assertTrue(result.getPhotos().get(0)
                    .contains("/uploads/files/"));
        }
    }

    /** ❌ update : actualité introuvable */
    @Test
    void testUpdate_ShouldThrow_WhenNotFound() {
        when(actualiteRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> actualiteService.update(99L, "Titre", "Desc", null));
    }

    /** ✅ delete : succès */
    @Test
    void testDelete_ShouldRemoveActualite() {
        Actualite act = new Actualite("Titre", "Desc", null);
        when(actualiteRepository.findById(1L)).thenReturn(Optional.of(act));

        Actualite result = actualiteService.delete(1L);

        verify(actualiteRepository).delete(act);
        assertEquals("Titre", result.getTitre());
    }

    /** ❌ delete : inexistante */
    @Test
    void testDelete_ShouldThrow_WhenNotFound() {
        when(actualiteRepository.findById(42L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> actualiteService.delete(42L));
    }
}
