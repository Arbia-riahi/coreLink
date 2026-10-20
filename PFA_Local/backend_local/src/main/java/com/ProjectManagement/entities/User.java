package com.ProjectManagement.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "matricule", nullable = false, unique = true, length = 20)
    private String matricule;

    @NotBlank
    @Size(max = 50)
    private String nom;

    @NotBlank
    @Size(max = 50)
    private String prenom;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(min = 8)
    @Column(nullable = false)
    private String motDePasse;

    @Enumerated(EnumType.STRING)
    private ERole role;

    private boolean estConnecte = false;

    private String image;

    @Column(name = "must_change_password")
    private boolean mustChangePassword = true;

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "reset_token_expiry")
    private LocalDateTime resetTokenExpiry;

    // Ajout du soft delete
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;
    
   
    public LocalDateTime getDateCreation() {
		return dateCreation;
	}
	public void setDateCreation(LocalDateTime dateCreation) {
		this.dateCreation = dateCreation;
	}
	@Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation;

    


    @ManyToMany(mappedBy = "membres")
    @JsonBackReference
    private List<Projet> projets = new ArrayList<>();

    @OneToMany(mappedBy = "chefProjet", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Projet> projetsGeres = new ArrayList<>();

    // --- Getters & Setters ---
    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }
	public String getMatricule() {
		return matricule;
	}
	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMotDePasse() {
		return motDePasse;
	}
	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}
	public ERole getRole() {
		return role;
	}
	public void setRole(ERole role) {
		this.role = role;
	}
	public boolean isEstConnecte() {
		return estConnecte;
	}
	public void setEstConnecte(boolean estConnecte) {
		this.estConnecte = estConnecte;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public boolean isMustChangePassword() {
		return mustChangePassword;
	}
	public void setMustChangePassword(boolean mustChangePassword) {
		this.mustChangePassword = mustChangePassword;
	}
	public String getResetToken() {
		return resetToken;
	}
	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}
	public LocalDateTime getResetTokenExpiry() {
		return resetTokenExpiry;
	}
	public void setResetTokenExpiry(LocalDateTime resetTokenExpiry) {
		this.resetTokenExpiry = resetTokenExpiry;
	}
	public List<Projet> getProjets() {
		return projets;
	}
	public void setProjets(List<Projet> projets) {
		this.projets = projets;
	}
	public List<Projet> getProjetsGeres() {
		return projetsGeres;
	}
	public void setProjetsGeres(List<Projet> projetsGeres) {
		this.projetsGeres = projetsGeres;
	}
	
	
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (!(o instanceof User)) return false;
	    User user = (User) o;
	    return matricule != null && matricule.equals(user.getMatricule());
	}

	@Override
	public int hashCode() {
	    return Objects.hash(matricule);
	}

	@Override
	public String toString() {
	    return "User{" +
	            "matricule='" + matricule + '\'' +
	            ", nom='" + nom + '\'' +
	            ", prenom='" + prenom + '\'' +
	            '}';
	}


  
}
