package com.ProjectManagement.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "taches")
public class Tache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   
    @Size(max = 100)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private E_EtatTache etat;

    @Enumerated(EnumType.STRING)
    private E_PrioriteTache priorite;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateFin;

    @Column(nullable = false)
    private boolean archived = false;

    @ManyToOne(fetch = FetchType.LAZY)
    private User assigneeA;
    @Column(name = "deadline_proche_notifiee")
    private boolean deadlineProcheNotifiee = false;

    @Column(name = "deadline_depassee_notifiee")
    private boolean deadlineDepasseeNotifiee = false;

    // Getters & setters
    public boolean isDeadlineProcheNotifiee() { return deadlineProcheNotifiee; }
    public void setDeadlineProcheNotifiee(boolean notified) { this.deadlineProcheNotifiee = notified; }

    public boolean isDeadlineDepasseeNotifiee() { return deadlineDepasseeNotifiee; }
    public void setDeadlineDepasseeNotifiee(boolean notified) { this.deadlineDepasseeNotifiee = notified; }


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "tache_membres",
        joinColumns = @JoinColumn(name = "tache_id"),
        inverseJoinColumns = @JoinColumn(name = "user_matricule", referencedColumnName = "matricule")
    )
    private List<User> autresAssignes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Projet projet;

    @OneToMany(mappedBy = "tache", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Commentaire> commentaires = new ArrayList<>();

    @Column
    private int tempsEstime;

    @Column
    private int tempsPasse;

    @Column
    private boolean enRetard = false;

    @Column
    private int nombreCommentaires = 0;

    @Column
    private int nombreModifications = 0;

    // --- Constructeur ---
    public Tache() {}

    // --- Lifecycle ---
    @PrePersist 
    //@PrePersist:dateCreation tjr définie auto lors de l'insertion, même si aucune valeur n'est fournie.
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now();
    }

    // --- Getters et Setters ---
    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public E_EtatTache getEtat() {
		return etat;
	}

	public void setEtat(E_EtatTache etat) {
		this.etat = etat;
	}

	public E_PrioriteTache getPriorite() {
		return priorite;
	}

	public void setPriorite(E_PrioriteTache priorite) {
		this.priorite = priorite;
	}

	public LocalDateTime getDateFin() {
		return dateFin;
	}

	public void setDateFin(LocalDateTime dateFin) {
		this.dateFin = dateFin;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public User getAssigneeA() {
		return assigneeA;
	}

	public void setAssigneeA(User assigneeA) {
		this.assigneeA = assigneeA;
	}

	public List<User> getAutresAssignes() {
		return autresAssignes;
	}

	public void setAutresAssignes(List<User> autresAssignes) {
		this.autresAssignes = autresAssignes;
	}

	public Projet getProjet() {
		return projet;
	}

	public void setProjet(Projet projet) {
		this.projet = projet;
	}

	public List<Commentaire> getCommentaires() {
		return commentaires;
	}

	public void setCommentaires(List<Commentaire> commentaires) {
		this.commentaires = commentaires;
	}

	public int getTempsEstime() {
		return tempsEstime;
	}

	public void setTempsEstime(int tempsEstime) {
		this.tempsEstime = tempsEstime;
	}

	public int getTempsPasse() {
		return tempsPasse;
	}

	public void setTempsPasse(int tempsPasse) {
		this.tempsPasse = tempsPasse;
	}

	public boolean isEnRetard() {
		return enRetard;
	}

	public void setEnRetard(boolean enRetard) {
		this.enRetard = enRetard;
	}

	public int getNombreCommentaires() {
		return nombreCommentaires;
	}

	public void setNombreCommentaires(int nombreCommentaires) {
		this.nombreCommentaires = nombreCommentaires;
	}

	public int getNombreModifications() {
		return nombreModifications;
	}

	public void setNombreModifications(int nombreModifications) {
		this.nombreModifications = nombreModifications;
	}

    
}
