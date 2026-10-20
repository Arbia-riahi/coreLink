package com.ProjectManagement.DTO;



import java.time.LocalDateTime;

public class NotificationDTO {
    private Long id;
    private String message;
    private String type;            // TypeNotification sous forme de String
    private boolean lue;
    private LocalDateTime date;
    private String destinataireMatricule;
    private Long tacheId;           // Optionnel
    private Long projetId;          // Optionnel

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isLue() { return lue; }
    public void setLue(boolean lue) { this.lue = lue; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public String getDestinataireMatricule() { return destinataireMatricule; }
    public void setDestinataireMatricule(String destinataireMatricule) { this.destinataireMatricule = destinataireMatricule; }

    public Long getTacheId() { return tacheId; }
    public void setTacheId(Long tacheId) { this.tacheId = tacheId; }

    public Long getProjetId() { return projetId; }
    public void setProjetId(Long projetId) { this.projetId = projetId; }
}

