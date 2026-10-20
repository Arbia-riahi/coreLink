package com.ProjectManagement.DTO;

public class ReactionDTO {
    private Long messageId;   // ID du message sur lequel la réaction est appliquée
    private String emoji;
    private MemberDTO user;
    private boolean removed; // indique si on supprime ou ajoute

    public ReactionDTO() {}

    public ReactionDTO(Long messageId, String emoji, MemberDTO user) {
        this.messageId = messageId;
        this.emoji = emoji;
        this.user = user;
    }

    public ReactionDTO(Long messageId, String emoji, MemberDTO user, boolean removed) {
        this.messageId = messageId;
        this.emoji = emoji;
        this.user = user;
        this.removed = removed;
    }

    public Long getMessageId() { return messageId; }
    public void setMessageId(Long messageId) { this.messageId = messageId; }

    public String getEmoji() { return emoji; }
    public void setEmoji(String emoji) { this.emoji = emoji; }

    public MemberDTO getUser() { return user; }
    public void setUser(MemberDTO user) { this.user = user; }

    public boolean isRemoved() { return removed; }
    public void setRemoved(boolean removed) { this.removed = removed; }
}
