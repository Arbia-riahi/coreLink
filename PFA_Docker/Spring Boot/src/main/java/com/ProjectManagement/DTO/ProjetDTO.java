package com.ProjectManagement.DTO;

import java.time.LocalDate;
import java.util.List;

public class ProjetDTO {
    private Long numeroProjet;
    private String titre;
    private String description;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String status;
    private String chefNomComplet;
    private List<MemberDTO> membres;
    private List<String> fichiers;
    private boolean archived;

    public ProjetDTO(Long numeroProjet, String titre, String description, LocalDate dateDebut, 
                     LocalDate dateFin, String status, String chefNomComplet, 
                     List<MemberDTO> membres, List<String> fichiers, boolean archived) {
        this.numeroProjet = numeroProjet;
        this.titre = titre;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.status = status;
        this.chefNomComplet = chefNomComplet;
        this.membres = membres;
        this.fichiers = fichiers;
        this.archived=archived;
    }

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

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getChefNomComplet() { return chefNomComplet; }
    public void setChefNomComplet(String chefNomComplet) { this.chefNomComplet = chefNomComplet; }

    public List<MemberDTO> getMembres() { return membres; }
    public void setMembres(List<MemberDTO> membres) { this.membres = membres; }

    public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public List<String> getFichiers() { return fichiers; }
    public void setFichiers(List<String> fichiers) { this.fichiers = fichiers; }
}
