package com.ProjectManagement.DTO;

import java.time.LocalDateTime;

public class CommentaireDTO {
    private Long id;
    private String contenu;
    private LocalDateTime date;
    private String auteurNom;
    private String auteurPrenom;
    private String auteurImage; // <-- Nouvelle propriété

    public CommentaireDTO(Long id, String contenu, LocalDateTime date, String auteurNom, String auteurPrenom, String auteurImage) {
        this.id = id;
        this.contenu = contenu;
        this.date = date;
        this.auteurNom = auteurNom;
        this.auteurPrenom = auteurPrenom;
        this.auteurImage = auteurImage;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public String getAuteurNom() { return auteurNom; }
    public void setAuteurNom(String auteurNom) { this.auteurNom = auteurNom; }

    public String getAuteurPrenom() { return auteurPrenom; }
    public void setAuteurPrenom(String auteurPrenom) { this.auteurPrenom = auteurPrenom; }

    public String getAuteurImage() { return auteurImage; }
    public void setAuteurImage(String auteurImage) { this.auteurImage = auteurImage; }
}
