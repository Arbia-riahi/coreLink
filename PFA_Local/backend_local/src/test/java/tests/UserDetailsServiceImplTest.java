package tests;

import com.ProjectManagement.entities.*;
import com.ProjectManagement.repositories.UserRepository;
import com.ProjectManagement.services.*;
import com.ProjectManagement.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mail.MailSendException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserDetailsServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // 🔥 injection manuelle car @Autowired ne marche pas dans @InjectMocks
        userService = new UserDetailsServiceImpl(userRepository, passwordEncoder);
        userService.emailService = emailService; // 👈 ajout clé
    }


    /** ✅ loadUserByUsername : succès */
    @Test
    void testLoadUserByUsername_Success() {
        User user = new User();
        user.setMatricule("M123");
        user.setDeleted(false);
        user.setRole(ERole.MEMBER); 

        when(userRepository.findByMatricule("M123")).thenReturn(Optional.of(user));

        assertNotNull(userService.loadUserByUsername("M123"));
    }

    /** ❌ loadUserByUsername : utilisateur inexistant */
    @Test
    void testLoadUserByUsername_NotFound() {
        when(userRepository.findByMatricule("ABC")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("ABC"));
    }

    /** ❌ loadUserByUsername : utilisateur supprimé */
    @Test
    void testLoadUserByUsername_Deleted() {
        User user = new User();
        user.setDeleted(true);
        when(userRepository.findByMatricule("X001")).thenReturn(Optional.of(user));
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("X001"));
    }

    /** ✅ createUser : succès */
    @Test
    void testCreateUser_Success() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getOriginalFilename()).thenReturn("photo.png");
        when(mockFile.getInputStream()).thenReturn(InputStream.nullInputStream());
        when(mockFile.getContentType()).thenReturn("image/png");

        when(userRepository.existsByMatricule("U123")).thenReturn(false);
        when(userRepository.findByEmail("user@mail.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("ENCODED_PASS");
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.exists(any(Path.class))).thenReturn(true);
            mockedFiles.when(() -> Files.copy(
                    any(InputStream.class),
                    any(Path.class),
                    any(CopyOption[].class)
            )).thenReturn(1L);

            User result = userService.createUser("U123", "Nom", "Prenom",
                    "user@mail.com", "Azerty@123", ERole.MEMBER, mockFile);

            assertNotNull(result);
            assertEquals("U123", result.getMatricule());
            verify(userRepository).save(any(User.class));
        }
    }

    /** ❌ createUser : email déjà utilisé */
    @Test
    void testCreateUser_EmailDejaUtilise() {
        when(userRepository.existsByMatricule("U123")).thenReturn(false);
        when(userRepository.findByEmail("user@mail.com")).thenReturn(Optional.of(new User()));
        assertThrows(RuntimeException.class, () -> userService.createUser(
                "U123", "Nom", "Prenom", "user@mail.com", "Azerty@123", ERole.MEMBER, null
        ));
    }

    /** ✅ updateUser : succès */
    @Test
    void testUpdateUser_Success() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getOriginalFilename()).thenReturn("photo.png");
        when(mockFile.getInputStream()).thenReturn(InputStream.nullInputStream());

        User existing = new User();
        existing.setMatricule("U123");
        existing.setEmail("old@mail.com");

        when(userRepository.findByMatricule("U123")).thenReturn(Optional.of(existing));
        when(userRepository.findByEmail("new@mail.com")).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.exists(any(Path.class))).thenReturn(true);
            mockedFiles.when(() -> Files.copy(any(InputStream.class), any(Path.class), any(CopyOption[].class)))
                    .thenReturn(1L);

            User updated = userService.updateUser("U123", "Nom", "Prenom", "new@mail.com", ERole.MANAGER, mockFile);
            assertEquals("Nom", updated.getNom());
            verify(userRepository).save(any(User.class));
        }
    }

    /** ❌ updateUser : utilisateur introuvable */
    @Test
    void testUpdateUser_UserNotFound() {
        when(userRepository.findByMatricule("U999")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,
                () -> userService.updateUser("U999", "Nom", "Prenom", "mail@mail.com", ERole.MEMBER, null));
    }

    /** ✅ verifyOldPassword : succès */
    @Test
    void testVerifyOldPassword_Success() {
        User u = new User();
        u.setMotDePasse("ENCODED");
        when(userRepository.findByMatricule("U1")).thenReturn(Optional.of(u));
        when(passwordEncoder.matches("plain", "ENCODED")).thenReturn(true);
        assertTrue(userService.verifyOldPassword("U1", "plain"));
    }

    /** ❌ verifyOldPassword : user inexistant */
    @Test
    void testVerifyOldPassword_UserNotFound() {
        when(userRepository.findByMatricule("404")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.verifyOldPassword("404", "plain"));
    }

    /** ✅ changePassword : succès */
    @Test
    void testChangePassword_Success() {
        User u = new User();
        u.setMatricule("U123");
        u.setMotDePasse("OLD_HASH");

        when(userRepository.findByMatricule("U123")).thenReturn(Optional.of(u));
        when(passwordEncoder.matches("OLD", "OLD_HASH")).thenReturn(true);
        when(passwordEncoder.encode("NewPass@123")).thenReturn("NEW_HASH");

        userService.changePassword("U123", "OLD", "NewPass@123");

        verify(userRepository).save(any(User.class));
    }


    /** ❌ changePassword : ancien mot de passe incorrect */
    @Test
    void testChangePassword_WrongOldPassword() {
        User u = new User();
        u.setMotDePasse("HASHED");
        when(userRepository.findByMatricule("U123")).thenReturn(Optional.of(u));
        when(passwordEncoder.matches("OLD", "HASHED")).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> userService.changePassword("U123", "OLD", "New@123"));
    }

    /** ✅ initiatePasswordReset : succès */
    @Test
    void testInitiatePasswordReset_Success() throws Exception {
        User u = new User();
        u.setEmail("user@mail.com");
        u.setNom("Dupont");
        when(userRepository.findByEmail("user@mail.com")).thenReturn(Optional.of(u));

        userService.initiatePasswordReset("user@mail.com");

        verify(emailService).send(eq("user@mail.com"), anyString(), contains("réinitialiser"));
        verify(userRepository).save(u);
    }

    /** ❌ initiatePasswordReset : e-mail invalide (MailSendException) */
    @Test
    void testInitiatePasswordReset_MailSendException() throws Exception {
        User u = new User();
        u.setEmail("user@mail.com");
        u.setNom("Dupont");
        when(userRepository.findByEmail("user@mail.com")).thenReturn(Optional.of(u));
        doThrow(new MailSendException("Erreur SMTP"))
                .when(emailService).send(anyString(), anyString(), anyString());

        assertThrows(RuntimeException.class, () -> userService.initiatePasswordReset("user@mail.com"));
    }

    /** ✅ getUsersRoleDistribution */
    @Test
    void testGetUsersRoleDistribution() {
        when(userRepository.countByRoleAndIsDeletedFalse(ERole.MANAGER)).thenReturn(2L);
        when(userRepository.countByRoleAndIsDeletedFalse(ERole.MEMBER)).thenReturn(5L);

        Map<String, Long> result = userService.getUsersRoleDistribution();

        assertEquals(2L, result.get("Managers"));
        assertEquals(5L, result.get("Members"));
    }
}
