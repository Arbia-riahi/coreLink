package com.ProjectManagement.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "commentaires")
public class Commentaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String contenu;

    @Column(nullable = false)
    private LocalDateTime date = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    private User auteur;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tache tache;

    // Réponse à un autre commentaire
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Commentaire parentComment;

    @ManyToMany
    @JoinTable(
        name = "commentaire_mentions",
        joinColumns = @JoinColumn(name = "commentaire_id"),
        inverseJoinColumns = @JoinColumn(name = "user_matricule")
    )
    private List<User> mentions = new ArrayList<>();

    private boolean modifie = false; 
    private boolean supprime = false;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getContenu() {
		return contenu;
	}
	public void setContenu(String contenu) {
		this.contenu = contenu;
	}
	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	public User getAuteur() {
		return auteur;
	}
	public void setAuteur(User auteur) {
		this.auteur = auteur;
	}
	public Tache getTache() {
		return tache;
	}
	public void setTache(Tache tache) {
		this.tache = tache;
	}
	public Commentaire getParentComment() {
		return parentComment;
	}
	public void setParentComment(Commentaire parentComment) {
		this.parentComment = parentComment;
	}
	public List<User> getMentions() {
		return mentions;
	}
	public void setMentions(List<User> mentions) {
		this.mentions = mentions;
	}
	public boolean isModifie() {
		return modifie;
	}
	public void setModifie(boolean modifie) {
		this.modifie = modifie;
	}
	public boolean isSupprime() {
		return supprime;
	}
	public void setSupprime(boolean supprime) {
		this.supprime = supprime;
	} 

   
}

