package com.ProjectManagement.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_id", referencedColumnName = "matricule", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)  // Si un utilisateur est supprimé, ses messages le seront aussi
    private User sender; // Utilisateur qui envoie le message

    @Column(columnDefinition = "TEXT")
    private String content;  // Contenu du message (texte)

    @Column(name = "file_url")
    private String fileUrl;  // URL du fichier (image/vidéo)
    
    @Column(name = "audio_url")
    private String audioUrl;  // URL du message vocal
    
    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Reaction> reactions = new HashSet<>();

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();  // Date/heure du message

    // Liste des utilisateurs qui ont vu ce message
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "message_seen_by",
        joinColumns = @JoinColumn(name = "message_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> seenBy = new HashSet<>();
    
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "message_mentions",
        joinColumns = @JoinColumn(name = "message_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> mentionedUsers = new HashSet<>();

   

    // Constructeurs
    public Message() {}

    public Message(User sender, String content, String fileUrl) {
        this.sender = sender;
        this.content = content;
        this.fileUrl = fileUrl;
        this.timestamp = LocalDateTime.now();
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getSender() { return sender; }
    public void setSender(User sender) { this.sender = sender; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Set<User> getSeenBy() { return seenBy; }
    public void setSeenBy(Set<User> seenBy) { this.seenBy = seenBy; }

    public String getAudioUrl() { return audioUrl; }
    public void setAudioUrl(String audioUrl) { this.audioUrl = audioUrl; }

    public Set<Reaction> getReactions() { return reactions; }
    public void setReactions(Set<Reaction> reactions) { this.reactions = reactions; }
    public Set<User> getMentionedUsers() {
        return mentionedUsers;
    }

    public void setMentionedUsers(Set<User> mentionedUsers) {
        this.mentionedUsers = mentionedUsers;
    }

}
