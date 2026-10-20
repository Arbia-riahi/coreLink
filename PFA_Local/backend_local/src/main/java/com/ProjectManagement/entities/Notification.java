package com.ProjectManagement.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String message; // Contenu de la notification

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 50, nullable = false)
    private TypeNotification type;


    @Column(nullable = false)
    private boolean lue = false; // Notification vue ou pas

    @Column(nullable = false)
    private LocalDateTime date = LocalDateTime.now(); // Date de création

    @ManyToOne(fetch = FetchType.LAZY)
    private User destinataire; // L'utilisateur ciblé

    @ManyToOne(fetch = FetchType.LAZY)
    private Tache tacheLiee; // Optionnel, lien avec une tâche

    @ManyToOne(fetch = FetchType.LAZY)
    private Projet projetLie; // Optionnel, lien avec un projet

    // -------- Getters & Setters --------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public TypeNotification getType() { return type; }
    public void setType(TypeNotification type) { this.type = type; }

    public boolean isLue() { return lue; }
    public void setLue(boolean lue) { this.lue = lue; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public User getDestinataire() { return destinataire; }
    public void setDestinataire(User destinataire) { this.destinataire = destinataire; }

    public Tache getTacheLiee() { return tacheLiee; }
    public void setTacheLiee(Tache tacheLiee) { this.tacheLiee = tacheLiee; }

    public Projet getProjetLie() { return projetLie; }
    public void setProjetLie(Projet projetLie) { this.projetLie = projetLie; }
}
