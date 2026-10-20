package com.ProjectManagement.DTO;

import java.time.LocalDateTime;
import java.util.*;

public class TacheDTO {
    private Long id;
    private String titre;
    private String description;
    private String etat;        // E_EtatTache sous forme de String
    private String priorite;    // E_PrioriteTache sous forme de String
    private LocalDateTime dateCreation;
    private LocalDateTime dateFin;
    private String assigneeMatricule;  // Clé primaire du User
    private List<MemberDTO> autresAssignes; // Liste des matricules des autres utilisateurs
    private Long projetId;       // ID du projet
    private int tempsEstime;
    private int tempsPasse;
    private boolean enRetard;
    private int nombreCommentaires;
    private int nombreModifications;
    
    public TacheDTO() {
        this.autresAssignes = new ArrayList<>();
    }


    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getEtat() { return etat; }
    public void setEtat(String etat) { this.etat = etat; }

    public String getPriorite() { return priorite; }
    public void setPriorite(String priorite) { this.priorite = priorite; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public LocalDateTime getDateFin() { return dateFin; }
    public void setDateFin(LocalDateTime dateFin) { this.dateFin = dateFin; }

    public String getAssigneeMatricule() { return assigneeMatricule; }
    public void setAssigneeMatricule(String assigneeMatricule) { this.assigneeMatricule = assigneeMatricule; }

    public List<MemberDTO> getAutresAssignes() { return autresAssignes; }
    public void setAutresAssignes(List<MemberDTO> autresAssignes) { this.autresAssignes = autresAssignes; }

    public Long getProjetId() { return projetId; }
    public void setProjetId(Long projetId) { this.projetId = projetId; }

    public int getTempsEstime() { return tempsEstime; }
    public void setTempsEstime(int tempsEstime) { this.tempsEstime = tempsEstime; }

    public int getTempsPasse() { return tempsPasse; }
    public void setTempsPasse(int tempsPasse) { this.tempsPasse = tempsPasse; }

    public boolean isEnRetard() { return enRetard; }
    public void setEnRetard(boolean enRetard) { this.enRetard = enRetard; }

    public int getNombreCommentaires() { return nombreCommentaires; }
    public void setNombreCommentaires(int nombreCommentaires) { this.nombreCommentaires = nombreCommentaires; }

    public int getNombreModifications() { return nombreModifications; }
    public void setNombreModifications(int nombreModifications) { this.nombreModifications = nombreModifications; }
}