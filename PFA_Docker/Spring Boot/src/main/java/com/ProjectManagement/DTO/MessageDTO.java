package com.ProjectManagement.DTO;

import java.time.LocalDateTime;
import java.util.List;

public class MessageDTO {
    private Long id;  // ID du message
    private MemberDTO sender;
    private String content;
    private String fileUrl;
    private String audioUrl;
    private LocalDateTime timestamp;
    private List<MemberDTO> seenBy;  // Liste des utilisateurs ayant vu
    private List<ReactionDTO> reactions;  // Liste des réactions
    private List<MemberDTO> mentions;

   


    public MessageDTO() {}

    public MessageDTO(Long id, MemberDTO sender, String content, String fileUrl,
                      String audioUrl, LocalDateTime timestamp, List<MemberDTO> seenBy) {
        this.id = id;
        this.sender = sender;
        this.content = content;
        this.fileUrl = fileUrl;
        this.audioUrl = audioUrl;
        this.timestamp = timestamp;
        this.seenBy = seenBy;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public MemberDTO getSender() { return sender; }
    public void setSender(MemberDTO sender) { this.sender = sender; }

    public String getAudioUrl() { return audioUrl; }
    public void setAudioUrl(String audioUrl) { this.audioUrl = audioUrl; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public List<MemberDTO> getSeenBy() { return seenBy; }
    public void setSeenBy(List<MemberDTO> seenBy) { this.seenBy = seenBy; }

    public List<ReactionDTO> getReactions() { return reactions; }
    public void setReactions(List<ReactionDTO> reactions) { this.reactions = reactions; }
    public List<MemberDTO> getMentions() {
        return mentions;
    }

    public void setMentions(List<MemberDTO> mentions) {
        this.mentions = mentions;
    }
}
