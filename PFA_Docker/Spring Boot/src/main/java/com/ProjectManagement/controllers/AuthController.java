package com.ProjectManagement.controllers;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ProjectManagement.entities.ERole;
import com.ProjectManagement.entities.User;
import com.ProjectManagement.jwt.JwtUtils;
import com.ProjectManagement.models.request.LoginRequest;
import com.ProjectManagement.models.request.SignupRequest;
import com.ProjectManagement.models.response.JwtResponse;
import com.ProjectManagement.repositories.UserRepository;
import com.ProjectManagement.services.UserDetailsImpl;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  PasswordEncoder encoder;
  
  @Autowired
  JwtUtils jwtUtils;
  
  @PostMapping("/signin")
public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    try {
        // Vérification champs vides
        if (loginRequest.getMatricule() == null || loginRequest.getMatricule().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Erreur : Le matricule est obligatoire.");
        }
        if (loginRequest.getMotDePasse() == null || loginRequest.getMotDePasse().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Erreur : Le mot de passe est obligatoire.");
        }

        //champs incorrects
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getMatricule(),
                    loginRequest.getMotDePasse()
                )
            );
        } catch (Exception e) {
            // Matricule ou mot de passe incorrect
            return ResponseEntity.status(401).body("Erreur : Matricule ou mot de passe incorrect.");
        }

        // Si OK → génération du token
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findByMatricule(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        return ResponseEntity.ok(new JwtResponse(
            jwt,
            user.getMatricule(),
            user.getNom(),
            user.getPrenom(),
            user.getEmail(),
            user.getImage(),
            userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()),
            user.isMustChangePassword()
        ));
    } catch (Exception ex) {
        // Gestion panne serveur (DB down, AuthManager HS, etc.)
        return ResponseEntity.status(503).body("Erreur : Service d'authentification indisponible. Veuillez réessayer plus tard.");
    }
}


  
  
  
  
  //pour inscrire admin:
  @PostMapping("/signup")
  public ResponseEntity<?> registerAdmin(@Valid @RequestBody SignupRequest signUpRequest) {

      // Vérifier unicité matricule et email
      if (userRepository.existsByMatricule(signUpRequest.getMatricule())) {
          return ResponseEntity.badRequest().body("Erreur: Matricule déjà utilisé !");
      }


      // Créer un nouvel utilisateur ADMIN
      User user = new User();
      user.setMatricule(signUpRequest.getMatricule());
      user.setNom(signUpRequest.getNom());
      user.setPrenom(signUpRequest.getPrenom());
      user.setEmail(signUpRequest.getEmail());
      user.setMotDePasse(encoder.encode(signUpRequest.getMotDePasse())); // 🔐 BCrypt ici
      user.setRole(ERole.ADMIN);
     

      userRepository.save(user);

      return ResponseEntity.ok("Admin enregistré avec succès !");
  }

}

     

   