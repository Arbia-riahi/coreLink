package tests;

import com.ProjectManagement.DTO.*;
import com.ProjectManagement.entities.*;
import com.ProjectManagement.services.*;
import com.ProjectManagement.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageServiceTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReactionRepository reactionRepository;

    @InjectMocks
    private MessageService messageService;

    private User user;
    private Message message;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setMatricule("U123");
        user.setMatricule("U001");
        user.setNom("Doe");
        user.setPrenom("John");
        user.setImage("img.png");

        message = new Message();
        message.setId(10L);
        message.setSender(user);
        message.setContent("Hello world!");
        message.setFileUrl(null);
        message.setAudioUrl(null);
        message.setTimestamp(LocalDateTime.now());
        message.setSeenBy(new HashSet<>());
        message.setReactions(new HashSet<>());
    }

    // -------------------------------------------------------------
    // ✅ TEST saveMessage()
    // -------------------------------------------------------------
    @Test
    void saveMessage_shouldSaveMessageSuccessfully() {
        when(userRepository.findByMatricule("U001")).thenReturn(Optional.of(user));
        when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> {
            Message msg = invocation.getArgument(0);
            msg.setId(1L);
            return msg;
        });

        MessageDTO dto = messageService.saveMessage("U001", "Hello @Doe John", null, null);

        assertNotNull(dto);
        assertEquals("Hello @Doe John", dto.getContent());
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    // -------------------------------------------------------------
    // ✅ TEST getAllMessages()
    // -------------------------------------------------------------
    @Test
    void getAllMessages_shouldReturnListOfMessages() {
        when(messageRepository.findAll()).thenReturn(List.of(message));

        List<MessageDTO> dtos = messageService.getAllMessages();

        assertEquals(1, dtos.size());
        assertEquals("Hello world!", dtos.get(0).getContent());
    }

    // -------------------------------------------------------------
    // ✅ TEST markMessageAsRead()
    // -------------------------------------------------------------
    @Test
    void markMessageAsRead_shouldAddReaderAndPublishEvent() {
        when(messageRepository.findById(10L)).thenReturn(Optional.of(message));
        when(userRepository.findByMatricule("U001")).thenReturn(Optional.of(user));

        messageService.markMessageAsRead(10L, "U001");

        assertTrue(message.getSeenBy().contains(user));
        verify(messagingTemplate, times(1))
                .convertAndSend(eq("/topic/message-seen"), any(Map.class));
    }

    // -------------------------------------------------------------
    // ✅ TEST getSeenBy()
    // -------------------------------------------------------------
    @Test
    void getSeenBy_shouldReturnListOfMembers() {
        message.getSeenBy().add(user);
        when(messageRepository.findById(10L)).thenReturn(Optional.of(message));

        List<MemberDTO> seenBy = messageService.getSeenBy(10L);

        assertEquals(1, seenBy.size());
        assertEquals("Doe", seenBy.get(0).getNom());
    }

    // -------------------------------------------------------------
    // ✅ TEST countUnreadMessages()
    // -------------------------------------------------------------
    @Test
    void countUnreadMessages_shouldReturnCount() {
        when(userRepository.findByMatricule("U001")).thenReturn(Optional.of(user));
        when(messageRepository.countBySeenByNotContainingAndSenderNot(user, user)).thenReturn(3L);

        long count = messageService.countUnreadMessages("U001");

        assertEquals(3L, count);
    }

    // -------------------------------------------------------------
    // ✅ TEST addReaction() -> ajout
    // -------------------------------------------------------------
    @Test
    void addReaction_shouldAddNewReaction() {
        when(messageRepository.findById(10L)).thenReturn(Optional.of(message));
        when(userRepository.findByMatricule("U001")).thenReturn(Optional.of(user));
        when(reactionRepository.findByMessageAndUserAndType(any(), any(), any())).thenReturn(Optional.empty());

        ReactionDTO dto = messageService.addReaction(10L, "👍", "U001");

        assertNotNull(dto);
        assertFalse(dto.isRemoved());
        verify(reactionRepository, times(1)).save(any(Reaction.class));
        verify(messagingTemplate).convertAndSend(eq("/topic/reactions"), any(ReactionDTO.class));
    }

    // -------------------------------------------------------------
    // ✅ TEST addReaction() -> suppression
    // -------------------------------------------------------------
    @Test
    void addReaction_shouldRemoveExistingReaction() {
        Reaction existing = new Reaction(ReactionType.fromEmoji("👍"), user, message);
        when(messageRepository.findById(10L)).thenReturn(Optional.of(message));
        when(userRepository.findByMatricule("U001")).thenReturn(Optional.of(user));
        when(reactionRepository.findByMessageAndUserAndType(any(), any(), any()))
                .thenReturn(Optional.of(existing));

        ReactionDTO dto = messageService.addReaction(10L, "👍", "U001");

        assertTrue(dto.isRemoved());
        verify(reactionRepository).delete(existing);
    }

    // -------------------------------------------------------------
    // ✅ TEST extractMentionedUsers() via réflexion (méthode privée)
    // -------------------------------------------------------------
    @Test
    void extractMentionedUsers_shouldDetectUserMentions() throws Exception {
        when(userRepository.findByNomAndPrenom("Doe", "John")).thenReturn(Optional.of(user));

        var method = MessageService.class.getDeclaredMethod("extractMentionedUsers", String.class);
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        Set<User> result = (Set<User>) method.invoke(messageService, "@Doe John @Jane Doe");

        assertEquals(1, result.size());
        assertTrue(result.contains(user));
    }
}
