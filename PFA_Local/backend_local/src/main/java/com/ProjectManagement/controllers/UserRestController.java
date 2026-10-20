package com.ProjectManagement.controllers;

import com.ProjectManagement.DTO.MemberDTO;
import com.ProjectManagement.entities.ERole;
import com.ProjectManagement.entities.User;
import com.ProjectManagement.repositories.UserRepository;
import com.ProjectManagement.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.*;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.*;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/users")
@CrossOrigin("*")
public class UserRestController {

	private final UserDetailsServiceImpl userService;
	private final UserRepository userRepository;

	@Autowired
	public UserRestController(UserDetailsServiceImpl userService, UserRepository userRepository) {
		this.userService = userService;
		this.userRepository = userRepository;
	}

	// ====================================================================================
	// ADMIN – CRÉATION UTILISATEUR
	// ====================================================================================
	@Operation(summary = "Créer un utilisateur (ADMIN uniquement)", description = """
			Création d’un utilisateur via un Admin.

			✔ Champs obligatoires : matricule, nom, prenom, email, motDePasse, rôle
			✔ Image optionnelle

			🔎 Tests QA :
			- Email déjà existant → BAD_REQUEST
			- Matricule déjà existant → BAD_REQUEST
			- Upload d’image → OK
			- ADMIN seulement → 403 si non admin
			""")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "Utilisateur créé"),
			@ApiResponse(responseCode = "400", description = "Erreur validation") })
	@PostMapping("/admin/create")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<?> createUserByAdmin(@RequestParam String matricule, @RequestParam String nom,
			@RequestParam String prenom, @RequestParam String email, @RequestParam String motDePasse,
			@RequestParam ERole role, @RequestParam(required = false) MultipartFile image) {
		try {
			User created = userService.createUser(matricule, nom, prenom, email, motDePasse, role, image);
			return ResponseEntity.status(HttpStatus.CREATED).body(created);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
		}
	}

	// ====================================================================================
	// ADMIN – LISTE UTILISATEURS (AVEC IMAGE FULL URL)
	// ====================================================================================
	@Operation(summary = "Lister tous les utilisateurs (ADMIN)", description = """
			Retourne tous les utilisateurs actifs (isDeleted=false).

			✔ Ajoute automatiquement l’URL complète si l’image n’est pas absolue.
			""")
	@GetMapping("/admin/all")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> users = userRepository.findByIsDeletedFalse();

		users.forEach(user -> {
			if (user.getImage() != null && !user.getImage().startsWith("http")) {

				user.setImage("http://localhost:8080/uploads/files/" + user.getImage());

			}
		});
		return ResponseEntity.ok(users);
	}

	// ====================================================================================
	// TOUS – USERS POUR MENTIONS @
	// ====================================================================================
	@Operation(summary = "Liste allégée des utilisateurs (mentions @)", description = """
			Utilisé pour la fonctionnalité @mention.
			Retourne MemberDTO (matricule, nom, prenom, image).
			""")
	@GetMapping("/all")
	public List<MemberDTO> getAllUsersForMention() {
		return userRepository.findAll().stream()
				.map(user -> new MemberDTO(user.getMatricule(), user.getNom(), user.getPrenom(), user.getImage()))
				.toList();
	}

	// ====================================================================================
	// PROFIL – VÉRIFIER ANCIEN MOT DE PASSE
	// ====================================================================================
	@Operation(summary = "Vérifier l’ancien mot de passe", description = """
			Vérifie si l’ancien mot de passe entré par l’utilisateur est correct.

			Possible via :
			✔ RequestParam
			✔ Body JSON

			🔎 Tests QA :
			- Scenario mauvais mot de passe
			- Scenario mot de passe vide
			""")
	@PostMapping("/verify-old-password")
     public ResponseEntity<Map<String, Object>> verifyOldPassword(
        @RequestBody Map<String, String> body) {

    try {
        String oldPassword = body.get("oldPassword");

        if (oldPassword == null || oldPassword.isBlank()) {
            return ResponseEntity.badRequest().body(
                Map.of("valid", false, "message", "Paramètre 'oldPassword' manquant.")
            );
        }

        String matricule = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean valid = userService.verifyOldPassword(matricule, oldPassword);

        if (!valid) {
            return ResponseEntity.badRequest().body(
                Map.of("valid", false, "message", "Ancien mot de passe incorrect !")
            );
        }

        return ResponseEntity.ok(Map.of("valid", true));

    } catch (Exception e) {
        return ResponseEntity.badRequest().body(
            Map.of("valid", false, "message", "Erreur : " + e.getMessage())
        );
    }
}


	private String escapeLike(String input) {
		if (input == null)
			return "";
		return input.replace("!", "!!") // on échappe l’escape lui-même
				.replace("%", "!%") // % littéral
				.replace("_", "!_"); // _ littéral
	}

	// ====================================================================================
	// ADMIN – PAGINATION / RECHERCHE UTILISATEURS
	// ====================================================================================
	@Operation(summary = "Pagination + recherche (ADMIN)", description = """
			Permet de paginer les utilisateurs et filtrer selon un mot-clé :
			✔ nom
			✔ prenom
			✔ email
			✔ matricule

			🔎 Tests QA :
			- Recherche insensible à la casse
			- Recherche caractère spécial (“%”, “_”)
			""")
	 @GetMapping("/admin/page")
     @PreAuthorize("hasAuthority('ADMIN')")
     public ResponseEntity<Page<User>> getUsersWithPagination(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "") String search) {

    var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreation"));
    String s = (search == null) ? "" : search.trim();

    Page<User> result = s.isEmpty()
        ? userRepository.findByRoleNotAndIsDeletedFalse(ERole.ADMIN, pageable)
        : userRepository.searchNonAdmin("%" + escapeLike(s.toLowerCase(Locale.ROOT)) + "%", ERole.ADMIN, pageable);

    // 👉 Uniformiser l'URL de l'image (comme tu fais dans /admin/all)
    result.getContent().forEach(u -> {
        if (u.getImage() != null && !u.getImage().startsWith("http")) {
            u.setImage("http://localhost:8080/uploads/files/" + u.getImage());
        }
    });

    return ResponseEntity.ok(result);
}

	// ====================================================================================
	// PROFIL – UPDATE USER CONNECTÉ
	// ====================================================================================
	@Operation(summary = "Modifier son propre profil", description = """
			Mise à jour des informations du profil utilisateur connecté.

			Champs modifiables :
			✔ nom
			✔ prenom
			✔ email
			✔ motDePasse
			✔ image

			🔎 Tests QA :
			- email déjà utilisé → 409
			- image → vérifier upload
			""")
	@PutMapping("/me/update")
	public ResponseEntity<?> updateMyProfile(@RequestParam(required = false) String nom,
			@RequestParam(required = false) String prenom, @RequestParam(required = false) String email,
			@RequestParam(required = false) String motDePasse, @RequestParam(required = false) MultipartFile image) {
		try {
			String matricule = SecurityContextHolder.getContext().getAuthentication().getName();
			User updated = userService.updateUserProfile(matricule, nom, prenom, email, motDePasse, image);
			return ResponseEntity.ok(updated);

		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Erreur : " + e.getMessage()));
		}
	}

	// ====================================================================================
	// ADMIN – UPDATE USER
	// ====================================================================================
	@Operation(summary = "Modifier un utilisateur (ADMIN)")
	@PutMapping("/admin/update/{matricule}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<?> updateUser(@PathVariable String matricule, @RequestParam String nom,
			@RequestParam String prenom, @RequestParam String email, @RequestParam ERole role,
			@RequestParam(required = false) MultipartFile image) {
		try {
			User updated = userService.updateUser(matricule, nom, prenom, email, role, image);
			return ResponseEntity.ok(updated);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Erreur lors de la mise à jour : " + e.getMessage());
		}
	}

	// ====================================================================================
	// ADMIN – SUPPRESSION LOGIQUE
	// ====================================================================================
	@Operation(summary = "Supprimer un utilisateur (soft delete)")
	@DeleteMapping("/admin/delete/{matricule}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<String> deleteUser(@PathVariable String matricule) {
		try {
			userService.softDeleteUser(matricule);
			return ResponseEntity.ok("Utilisateur désactivé avec succès (plus d'accès à la plateforme)");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Erreur lors de la suppression : " + e.getMessage());
		}
	}

	@GetMapping("/me")
	public ResponseEntity<User> getMyProfile() {
		String matricule = SecurityContextHolder.getContext().getAuthentication().getName();
		return userRepository.findByMatricule(matricule).map(ResponseEntity::ok)
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	// ====================================================================================
	// CHECK MATRICULE
	// ====================================================================================
	@Operation(summary = "Vérifier si un matricule existe déjà")
	@GetMapping("/check-matricule/{matricule}")
	public ResponseEntity<Boolean> checkMatriculeExists(@PathVariable String matricule) {
		return ResponseEntity.ok(userRepository.existsByMatricule(matricule));
	}

	// ====================================================================================
	// ADMIN – GET USER BY MATRICULE
	// ====================================================================================
	@Operation(summary = "Obtenir un utilisateur via son matricule (ADMIN)")
	@GetMapping("/admin/{matricule}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<?> getUserByMatricule(@PathVariable String matricule) {
		try {
			User user = userService.getUserByMatricule(matricule);
			if (user.getImage() != null && !user.getImage().startsWith("http")) {
				user.setImage("http://localhost:8080/uploads/files/" + user.getImage());
			}
			return ResponseEntity.ok(user);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé : " + e.getMessage());
		}
	}

	// ====================================================================================
	// MEMBER ONLY – LISTE MEMBRES (role=MEMBER)
	// ====================================================================================
	@Operation(summary = "Lister tous les membres (rôle MEMBER)")
	@GetMapping("/members")
	public ResponseEntity<List<User>> getAllMembers() {
		// Récupérer uniquement les utilisateurs ayant le rôle "MEMBER"
		List<User> members = userRepository.findByRoleAndIsDeletedFalse(ERole.MEMBER);

		// Ajouter l'URL complète des images si elles sont locales
		members.forEach(user -> {
			if (user.getImage() != null && !user.getImage().startsWith("http")) {
				user.setImage("http://localhost:8080/uploads/files/" + user.getImage());
			}
		});

		return ResponseEntity.ok(members);
	}

	// ====================================================================================
	// CHANGE PASSWORD
	// ====================================================================================
	@Operation(summary = "Changer son mot de passe")
	@PutMapping("/change-password")
	public ResponseEntity<?> changePassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
		try {
			String matricule = SecurityContextHolder.getContext().getAuthentication().getName();
			System.out.println("DEBUG >>> Matricule actuel = " + matricule);
			userService.changePassword(matricule, oldPassword, newPassword);
			return ResponseEntity.ok("Mot de passe changé avec succès");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
		}
	}

	// ====================================================================================
	// PASSWORD RESET – REQUEST MAIL
	// ====================================================================================
	@Operation(summary = "Demander un email de réinitialisation mot de passe")
	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(@RequestParam String email) {
		try {
			// On récupère l'utilisateur pour exposer son rôle dans la réponse
			User user = userRepository.findByEmail(email)
					.orElseThrow(() -> new RuntimeException("Aucun utilisateur trouvé avec cet email."));

			// Déclenche l'envoi du mail + génération token
			userService.initiatePasswordReset(email);

			String modulePath;
			if (user.getRole() == ERole.ADMIN) {
				modulePath = "backoffice";
			} else if (user.getRole() == ERole.MANAGER) {
				modulePath = "manager";
			} else {
				modulePath = "member";
			}

			Map<String, String> body = new HashMap<>();
			body.put("message", "Un email de réinitialisation a été envoyé à " + email);
			body.put("role", user.getRole().name()); // ⇦ rôle renvoyé à l'UI
			body.put("modulePath", modulePath); // ⇦ info utile pour l'UI si besoin

			return ResponseEntity.ok(body);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Erreur : " + e.getMessage()));
		}
	}

	// ====================================================================================
	// PASSWORD RESET
	// ====================================================================================
	@Operation(summary = "Réinitialiser mot de passe avec token")
	@PostMapping("/reset-password")
	public ResponseEntity<Map<String, String>> resetPassword(@RequestParam String token,
			@RequestParam String newPassword) {
		try {
			userService.resetPassword(token, newPassword);

			Map<String, String> response = new HashMap<>();
			response.put("message", "Mot de passe réinitialisé avec succès.");
			return ResponseEntity.ok(response);

		} catch (RuntimeException ex) {
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("message", ex.getMessage());
			return ResponseEntity.badRequest().body(errorResponse);
		}
	}

	// ====================================================================================
	// FORCE CHANGE PASSWORD
	// ====================================================================================
	@Operation(summary = "Forcer le changement du mot de passe (utilisé après premier login)")
	@PutMapping("/force-change-password")
	public ResponseEntity<?> forceChangePassword(@RequestParam String newPassword) {
		try {
			String matricule = SecurityContextHolder.getContext().getAuthentication().getName();
			userService.forceChangePassword(matricule, newPassword);
			return ResponseEntity.ok("Mot de passe changé avec succès");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
		}
	}

	// ====================================================================================
	// VALIDER RESET TOKEN (AVEC SIMULATION forceExpired)
	// ====================================================================================
	@Operation(summary = "Valider un token de réinitialisation de mot de passe", description = """
			Vérifie si le token est toujours valide.

			✔ Paramètre optionnel **forceExpired=true** → simule expiration

			🔎 Tests QA :
			- Token expiré
			- Token invalide
			- Simulation expiration (Selenium automatisation)
			""")
	@GetMapping("/validate-reset-token")
	public ResponseEntity<Map<String, String>> validateResetToken(@RequestParam String token,
			@RequestParam(required = false) Boolean forceExpired) { // 👈 paramètre optionnel
		try {
			// ✅ Si le flag forceExpired est passé dans l'URL, on simule l'expiration
			if (Boolean.TRUE.equals(forceExpired)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(Map.of("message", "Le lien de réinitialisation a expiré"));
			}

			// ✅ Logique réelle ensuite
			User user = userRepository.findByResetToken(token)
					.orElseThrow(() -> new RuntimeException("Le lien de réinitialisation a expiré"));

			if (user.getResetTokenExpiry() == null || user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(Map.of("message", "Le lien de réinitialisation a expiré"));
			}

			return ResponseEntity.ok(Map.of("message", "Token valide"));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
		}
	}

	// ====================================================================================
	// ADMIN – RÉPARTITION DES RÔLES
	// ====================================================================================
	@Operation(summary = "Obtenir la répartition des rôles (ADMIN uniquement)")
	@GetMapping("/role-distribution")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Map<String, Long>> getRoleDistribution() {
		return ResponseEntity.ok(userService.getUsersRoleDistribution());
	}

}