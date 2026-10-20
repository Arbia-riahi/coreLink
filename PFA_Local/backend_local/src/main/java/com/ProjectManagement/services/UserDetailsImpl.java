package com.ProjectManagement.services;

import com.ProjectManagement.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class UserDetailsImpl implements UserDetails {

    private String matricule;
    private String nom;
    private String prenom;
    private String email;
    private String image; // Ajout de l'image

    private boolean mustChangePassword; // Ajout du flag de changement obligatoire

    @JsonIgnore
    private String motDePasse;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(
            String matricule,
            String email,
            String motDePasse,
            String nom,
            String prenom,
            String image,
            boolean mustChangePassword,
            Collection<? extends GrantedAuthority> authorities) {
        this.matricule = matricule;
        this.email = email;
        this.motDePasse = motDePasse;
        this.nom = nom;
        this.prenom = prenom;
        this.image = image;
        this.mustChangePassword = mustChangePassword;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());
        return new UserDetailsImpl(
                user.getMatricule(),
                user.getEmail(),
                user.getMotDePasse(),
                user.getNom(),
                user.getPrenom(),
                user.getImage(),
                user.isMustChangePassword(),
                Collections.singletonList(authority)
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }

    public boolean isMustChangePassword() {
        return mustChangePassword;
    }

    @Override
    public String getPassword() {
        return motDePasse;
    }

    @Override
    public String getUsername() {
        return matricule;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDetailsImpl)) return false;
        UserDetailsImpl that = (UserDetailsImpl) o;
        return Objects.equals(matricule, that.matricule);
    }
}
