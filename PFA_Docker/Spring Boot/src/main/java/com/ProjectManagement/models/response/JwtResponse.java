package com.ProjectManagement.models.response;

import java.util.List;

public class JwtResponse {
    private String token;
    private String matricule;
    private String nom;
    private String prenom;
    private String email;
    private String image;
    private boolean mustChangePassword;
    private List<String> roles;

    public JwtResponse(String token, String matricule, String nom, String prenom,
                       String email, String image, List<String> roles, boolean mustChangePassword) {
        this.token = token;
        this.matricule = matricule;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.image = image;
        this.roles = roles;
        this.mustChangePassword = mustChangePassword;
    }

    // Getters et Setters
    public String getToken() { return token; }
    public String getMatricule() { return matricule; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public String getImage() { return image; }
    public boolean isMustChangePassword() { return mustChangePassword; }
    public List<String> getRoles() { return roles; }
}
