package com.ProjectManagement.services;

import com.ProjectManagement.entities.*;
import com.ProjectManagement.repositories.*;
import jakarta.mail.SendFailedException;
import jakarta.mail.internet.AddressException;
import org.eclipse.angus.mail.smtp.SMTPAddressFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public EmailService emailService;
	private final Path root = Paths.get("src/main/resources/static/uploads/files");
	

	@Autowired
	public UserDetailsServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserDetails loadUserByUsername(String matricule) throws UsernameNotFoundException {
		User user = userRepository.findByMatricule(matricule)
				.orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + matricule));

		if (user.isDeleted()) {
			throw new UsernameNotFoundException("Cet utilisateur a été désactivé.");
		}

		return UserDetailsImpl.build(user);
	}

	@Transactional
	public User createUser(String matricule, String nom, String prenom, String email, String motDePasse, ERole role,
			MultipartFile image) throws IOException {
		if (userRepository.existsByMatricule(matricule))
			throw new RuntimeException("Matricule déjà utilisé !");
		if (userRepository.findByEmail(email).isPresent())
			throw new RuntimeException("Email déjà utilisé !");
		if (!isPasswordStrong(motDePasse))
			throw new RuntimeException("Mot de passe faible.");
		if (!isValidEmail(email))
			throw new RuntimeException("Email invalide !");

		User user = new User();
		user.setMatricule(matricule);
		user.setNom(nom);
		user.setPrenom(prenom);
		user.setEmail(email);
		user.setRole(role);
		user.setMotDePasse(passwordEncoder.encode(motDePasse));
		user.setImage(saveImage(image));
		user.setMustChangePassword(true);
		user.setDateCreation(LocalDateTime.now());

		return userRepository.save(user);
	}

	@Transactional
	public User updateUser(String matricule, String nom, String prenom, String email, ERole role, MultipartFile image)
			throws IOException {

		User user = userRepository.findByMatricule(matricule)
				.orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

		Optional<User> existingUserWithEmail = userRepository.findByEmail(email);
		if (existingUserWithEmail.isPresent() && !existingUserWithEmail.get().getMatricule().equals(matricule)) {
			throw new RuntimeException("Email déjà utilisé !");
		}

		user.setNom(nom);
		user.setPrenom(prenom);
		user.setEmail(email);
		user.setRole(role);
		if (image != null && !image.isEmpty())
			user.setImage(saveImage(image));

		return userRepository.save(user);
	}

	@Transactional
	public User updateUserProfile(String matricule, String nom, String prenom, String email, String motDePasse,
			MultipartFile image) throws IOException {
		User user = userRepository.findByMatricule(matricule)
				.orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

		// Vérifier si l'email est modifié et déjà pris par un autre utilisateur
		if (email != null && !email.equals(user.getEmail())) {
			boolean emailExists = userRepository.existsByEmail(email);
			if (emailExists) {
				throw new IllegalArgumentException("Cet email existe déjà !");
			}
			user.setEmail(email);
		}

		if (nom != null)
			user.setNom(nom);
		if (prenom != null)
			user.setPrenom(prenom);
		if (motDePasse != null)
			user.setMotDePasse(passwordEncoder.encode(motDePasse));
		if (image != null && !image.isEmpty())
			user.setImage(saveImage(image));

		return userRepository.save(user);
	}

	private String saveImage(MultipartFile image) throws IOException {
		if (image == null || image.isEmpty())
			return null;
		if (!Files.exists(root))
			Files.createDirectories(root);
		String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
		Files.copy(image.getInputStream(), root.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
		return fileName;
	}

	private boolean isPasswordStrong(String password) {
		String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
		return password != null && password.matches(regex);
	}

	public boolean verifyOldPassword(String matricule, String oldPassword) {
		User user = userRepository.findByMatricule(matricule)
				.orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
		return passwordEncoder.matches(oldPassword, user.getMotDePasse());
	}

	private boolean isValidEmail(String email) {
		String regex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
		return email != null && email.matches(regex);
	}

	@Transactional(readOnly = true)
	public User getUserByMatricule(String matricule) {
		return userRepository.findByMatricule(matricule)
				.orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec le matricule : " + matricule));
	}

	@Transactional
	public void softDeleteUser(String matricule) {
		User user = userRepository.findByMatricule(matricule)
				.orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
		user.setDeleted(true);
		userRepository.save(user);
	}

	@Transactional
	public void changePassword(String matricule, String oldPassword, String newPassword) {
		User user = userRepository.findByMatricule(matricule)
				.orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

		System.out.println("DEBUG >> oldPassword reçu : " + oldPassword);
		System.out.println("DEBUG >> password actuel hashé : " + user.getMotDePasse());

		if (!passwordEncoder.matches(oldPassword, user.getMotDePasse())) {
			throw new RuntimeException("Ancien mot de passe incorrect !");
		}

		if (!isPasswordStrong(newPassword)) {
			throw new RuntimeException("Le mot de passe n'est pas assez sécurisé !");
		}

		user.setMotDePasse(passwordEncoder.encode(newPassword));
		user.setMustChangePassword(false);
		userRepository.save(user);

	}

	@Transactional
	public void initiatePasswordReset(String email)
			throws SMTPAddressFailedException, SendFailedException, AddressException {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("Aucun utilisateur trouvé avec cet email."));

		String token = UUID.randomUUID().toString();
		user.setResetToken(token);
		user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(30));
		userRepository.save(user);

		String resetLink = "http://localhost:58080/home/resetPassword?token=" + token;

		try {
			emailService.send(user.getEmail(), "Réinitialisation de votre mot de passe",
					"Bonjour " + user.getNom() + ",\n\n"
							+ "Cliquez sur ce lien pour réinitialiser votre mot de passe : " + resetLink + "\n\n"
							+ "Ce lien expirera dans 30 minutes.");

		} catch (MailSendException e) {
			// ⬅︎ adresse invalide / inexistante
			throw new RuntimeException("Adresse e-mail inexistante! utilisez une autre adresse");
		} catch (org.springframework.mail.MailException e) {
			// ⬅︎ panne du service SMTP
			throw new RuntimeException("Le service d’e-mail est momentanément indisponible. Réessayez plus tard.");
		}
	}

	@Transactional
	public void resetPassword(String token, String newPassword) {
		User user = userRepository.findByResetToken(token)
				.orElseThrow(() -> new RuntimeException("Token de réinitialisation invalide."));

		// Vérifier l'expiration du token
		if (user.getResetTokenExpiry() == null || user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
			throw new RuntimeException("Le lien de réinitialisation a expiré. Veuillez redemander un nouveau lien.");
		}

		if (!isPasswordStrong(newPassword)) {
			throw new RuntimeException("Le nouveau mot de passe n'est pas assez sécurisé.");
		}

		// Mettre à jour le mot de passe
		user.setMotDePasse(passwordEncoder.encode(newPassword));
		user.setResetToken(null);
		user.setResetTokenExpiry(null);
		userRepository.save(user);

		emailService.send(user.getEmail(), "Confirmation de changement de mot de passe",
				"Bonjour " + user.getNom() + ",\n\nVotre mot de passe a été changé avec succès.");
	}

	@Transactional
	public void forceChangePassword(String matricule, String newPassword) {
		User user = userRepository.findByMatricule(matricule)
				.orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

		if (!isPasswordStrong(newPassword)) {
			throw new RuntimeException("Le mot de passe n'est pas assez sécurisé !");
		}

		user.setMotDePasse(passwordEncoder.encode(newPassword));
		user.setMustChangePassword(false); // 🔥 Après le premier changement, plus besoin de forcer
		userRepository.save(user);

		userRepository.flush(); // 🔥 force la persistance immédiate

	}

	@Transactional(readOnly = true)
	public Map<String, Long> getUsersRoleDistribution() {
		long managers = userRepository.countByRoleAndIsDeletedFalse(ERole.MANAGER);
		long members = userRepository.countByRoleAndIsDeletedFalse(ERole.MEMBER);

		Map<String, Long> distribution = new HashMap<>();
		distribution.put("Managers", managers);
		distribution.put("Members", members);
		return distribution;
	}

}
