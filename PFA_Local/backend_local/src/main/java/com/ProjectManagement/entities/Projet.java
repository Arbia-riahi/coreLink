package com.ProjectManagement.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projets")
public class Projet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numero_projet")
    private Long numeroProjet;

    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 100)
    @Column(nullable = false)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;
    
    private boolean archived = false;
    
    @Column(name = "deadline_proche_notifiee")
    private boolean deadlineProcheNotifiee = false;

    @Column(name = "deadline_depassee_notifiee")
    private boolean deadlineDepasseeNotifiee = false;

    // Getters & setters
    public boolean isDeadlineProcheNotifiee() { return deadlineProcheNotifiee; }
    public void setDeadlineProcheNotifiee(boolean notified) { this.deadlineProcheNotifiee = notified; }

    public boolean isDeadlineDepasseeNotifiee() { return deadlineDepasseeNotifiee; }
    public void setDeadlineDepasseeNotifiee(boolean notified) { this.deadlineDepasseeNotifiee = notified; }


    public boolean isArchived() {
		return archived;
	}
	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	@Enumerated(EnumType.STRING)
    private EProjet status;

    // Chef de projet
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chef_projet_matricule", referencedColumnName = "matricule", nullable = false)
    @JsonBackReference // évite la boucle infinie avec User.projetsGeres
    private User chefProjet;

    // Liste des membres
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "projet_membres",
        joinColumns = @JoinColumn(name = "projet_id"),
        inverseJoinColumns = @JoinColumn(name = "user_matricule", referencedColumnName = "matricule")
    )
    @JsonManagedReference // côté principal pour la relation User.projets
    private List<User> membres = new ArrayList<>();

    



    // Liste des fichiers
    @ElementCollection
    @CollectionTable(name = "projet_fichiers", joinColumns = @JoinColumn(name = "projet_id"))
    @Column(name = "fichier")
    private List<String> fichiers = new ArrayList<>();

    // Getters et Setters
    public Long getNumeroProjet() { return numeroProjet; }
    public void setNumeroProjet(Long numeroProjet) { this.numeroProjet = numeroProjet; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public EProjet getStatus() { return status; }
    public void setStatus(EProjet status) { this.status = status; }

    public User getChefProjet() { return chefProjet; }
    public void setChefProjet(User chefProjet) { this.chefProjet = chefProjet; }

    public List<User> getMembres() { return membres; }
    public void setMembres(List<User> membres) { this.membres = membres; }

    public List<String> getFichiers() { return fichiers; }
    public void setFichiers(List<String> fichiers) { this.fichiers = fichiers; }
    
    @Transient
    public String getStatusLabel() {
        return (status != null) ? status.getLabel() : null;
    }

}
