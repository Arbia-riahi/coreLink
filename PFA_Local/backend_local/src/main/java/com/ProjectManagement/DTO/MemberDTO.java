package com.ProjectManagement.DTO;

import java.util.List;

public class MemberDTO {
    private String matricule;
    private String nom;
    private String prenom;
    private String image;

    public MemberDTO(String matricule, String nom, String prenom, String image) {
        this.matricule = matricule;
        this.nom = nom;
        this.prenom = prenom;
        this.image = image;
    }

    // Getters et Setters
    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}
