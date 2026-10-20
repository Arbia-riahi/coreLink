package com.ProjectManagement.services;

import com.ProjectManagement.DTO.MemberDTO;
import com.ProjectManagement.DTO.TacheDTO;
import com.ProjectManagement.entities.*;
import com.ProjectManagement.repositories.*;

import jakarta.persistence.EntityNotFoundException;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class TacheService {

	private final TacheRepository tacheRepository;
	private final NotificationService notificationService;
	private final UserRepository userRepository;
	private final ProjetRepository projetRepository;

	public TacheService(TacheRepository tacheRepository, NotificationService notificationService,
			UserRepository userRepository, ProjetRepository projetRepository) {
		this.tacheRepository = tacheRepository;
		this.notificationService = notificationService;
		this.userRepository = userRepository;
		this.projetRepository = projetRepository;
	}

	// --- Convertir Tache -> TacheDTO ---
	public TacheDTO toDTO(Tache tache) {
		TacheDTO dto = new TacheDTO();
		dto.setId(tache.getId());
		dto.setTitre(tache.getTitre());
		dto.setDescription(tache.getDescription());
		dto.setEtat(tache.getEtat() != null ? tache.getEtat().name() : null);
		dto.setPriorite(tache.getPriorite() != null ? tache.getPriorite().name() : null);
		dto.setDateCreation(tache.getDateCreation()); // <-- Ajouter ici
		dto.setDateFin(tache.getDateFin());

		// Assignee principal
		if (tache.getAssigneeA() != null) {
			dto.setAssigneeMatricule(tache.getAssigneeA().getMatricule());
			MemberDTO mainAssignee = new MemberDTO(tache.getAssigneeA().getMatricule(), tache.getAssigneeA().getNom(),
					tache.getAssigneeA().getPrenom(),
					tache.getAssigneeA().getImage() != null
							? "http://localhost:8080/uploads/files/" + tache.getAssigneeA().getImage()
							: null);
			dto.getAutresAssignes().add(0, mainAssignee);
		}

		// Autres assignés
		dto.setAutresAssignes(tache.getAutresAssignes().stream()
				.map(u -> new MemberDTO(u.getMatricule(), u.getNom(), u.getPrenom(),
						u.getImage() != null ? "http://localhost:8080/uploads/files/" + u.getImage() : null))
				.collect(Collectors.toList()));

		dto.setProjetId(tache.getProjet() != null ? tache.getProjet().getNumeroProjet() : null);
		dto.setTempsEstime(tache.getTempsEstime());
		dto.setTempsPasse(tache.getTempsPasse());
		dto.setEnRetard(tache.isEnRetard());
		dto.setNombreCommentaires(tache.getNombreCommentaires());
		dto.setNombreModifications(tache.getNombreModifications());
		return dto;
	}

	// --- Convertir TacheDTO -> Tache ---
	public Tache fromDTO(TacheDTO dto) {
		Tache tache = new Tache();
		tache.setTitre(dto.getTitre());
		tache.setDescription(dto.getDescription());
		if (dto.getEtat() == null || dto.getEtat().isBlank()) {
		    tache.setEtat(E_EtatTache.A_FAIRE); // ✅ valeur par défaut
		} else {
		    try {
		        tache.setEtat(E_EtatTache.valueOf(dto.getEtat().trim().toUpperCase()));
		    } catch (IllegalArgumentException e) {
		        // ✅ Si jamais le front envoie une valeur inconnue
		        tache.setEtat(E_EtatTache.A_FAIRE);
		    }
		}

		if (dto.getPriorite() == null || dto.getPriorite().isBlank()) {
		    tache.setPriorite(E_PrioriteTache.MOYENNE);
		} else {
		    try {
		        tache.setPriorite(E_PrioriteTache.valueOf(dto.getPriorite().trim().toUpperCase()));
		    } catch (IllegalArgumentException e) {
		        tache.setPriorite(E_PrioriteTache.MOYENNE);
		    }
		}

		tache.setDateFin(dto.getDateFin());
		// PAS de setDateCreation, géré par @PrePersist

		// Assignee principal
		if (dto.getAssigneeMatricule() != null) {
			User assignee = userRepository.findByMatricule(dto.getAssigneeMatricule()).orElseThrow(
					() -> new EntityNotFoundException("Utilisateur introuvable : " + dto.getAssigneeMatricule()));
			tache.setAssigneeA(assignee);
		}

		// Autres assignés
		if (dto.getAutresAssignes() != null && !dto.getAutresAssignes().isEmpty()) {
			List<User> autres = dto.getAutresAssignes().stream()
					.map(member -> userRepository.findByMatricule(member.getMatricule())
							.filter(u -> u.getRole() == ERole.MEMBER)
							.orElseThrow(() -> new EntityNotFoundException(
									"Utilisateur introuvable ou non autorisé : " + member.getMatricule())))
					.collect(Collectors.toList());
			tache.setAutresAssignes(autres);
		}

		// Projet + validation date
		if (dto.getProjetId() != null) {
			Projet projet = projetRepository.findById(dto.getProjetId())
					.orElseThrow(() -> new EntityNotFoundException("Projet introuvable : " + dto.getProjetId()));

			if (dto.getDateFin() != null && projet.getDateFin() != null
					&& !dto.getDateFin().toLocalDate().isBefore(projet.getDateFin())) {
				throw new IllegalArgumentException(
						"La date de fin de la tâche doit être strictement avant la date de fin du projet ("
								+ projet.getDateFin() + ").");
			}
			tache.setProjet(projet);
		}

		if (dto.getDateFin() != null) {
			LocalDateTime now = LocalDateTime.now(); // date de création
			if (!dto.getDateFin().isAfter(now)) {
				throw new IllegalArgumentException(
						"La date de fin de la tâche doit être postérieure à la date de création.");
			}
		}

		return tache;
	}

	// --- Recalculer le statut du projet ---
	@Transactional
	public void updateProjetStatus(Long projetId) {
		Projet projet = projetRepository.findById(projetId)
				.orElseThrow(() -> new EntityNotFoundException("Projet introuvable : " + projetId));

		List<Tache> taches = tacheRepository.findByProjet_NumeroProjetAndArchivedFalse(projetId);

		if (taches.isEmpty()) {
			projet.setStatus(EProjet.EN_ATTENTE);
		} else {
			boolean allAFaire = taches.stream().allMatch(t -> t.getEtat() == E_EtatTache.A_FAIRE);
			boolean allTermine = taches.stream().allMatch(t -> t.getEtat() == E_EtatTache.TERMINE);
			boolean allAnnule = taches.stream().allMatch(t -> t.getEtat() == E_EtatTache.ANNULE);

			if (allAFaire) {
				projet.setStatus(EProjet.EN_ATTENTE);
			} else if (allTermine) {
				projet.setStatus(EProjet.TERMINE);
			} else if (allAnnule) {
				projet.setStatus(EProjet.ANNULE);
			} else {
				projet.setStatus(EProjet.EN_COURS);
			}
		}

		projetRepository.save(projet);
	}

	// --- CRUD avec DTO ---
	public List<TacheDTO> getAllTachesDTO() {
		return tacheRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public Tache getTacheById(Long id) {
	    return tacheRepository.findById(id)
	            .orElseThrow(() -> new EntityNotFoundException("Tâche introuvable avec ID : " + id));
	}


	@Transactional
	public TacheDTO createTacheFromDTO(TacheDTO dto) {
		Tache tache = fromDTO(dto);
		Tache saved = tacheRepository.save(tache);

		// Mise à jour du statut du projet
		if (saved.getProjet() != null) {
			updateProjetStatus(saved.getProjet().getNumeroProjet());
		}

		// --- Notification pour l'assigné principal ---
		if (saved.getAssigneeA() != null) {
			notificationService.creerNotification(saved.getAssigneeA(),
					"Vous avez été assigné à la tâche : " + saved.getTitre(), TypeNotification.TACHE_ASSIGNATION,
					saved.getProjet(), saved);
		}

		// --- Notification pour les autres membres assignés ---
		if (saved.getAutresAssignes() != null && !saved.getAutresAssignes().isEmpty()) {
			for (User membre : saved.getAutresAssignes()) {
				notificationService.creerNotification(membre, "Vous avez été ajouté à la tâche : " + saved.getTitre(),
						TypeNotification.TACHE_ASSIGNATION, saved.getProjet(), saved);
			}
		}

		return toDTO(saved);
	}

	@Transactional
	public void archiveTache(Long id) {
		Tache existing = tacheRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Tâche non trouvée avec l'ID : " + id));
		existing.setArchived(true);
		Tache saved = tacheRepository.save(existing);

	}

	@Transactional
	public TacheDTO updateEtat(Long id, String nouvelEtat) {
		Tache existing = tacheRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Tâche non trouvée : " + id));
		existing.setEtat(E_EtatTache.valueOf(nouvelEtat));
		existing.setNombreModifications(existing.getNombreModifications() + 1);
		Tache updated = tacheRepository.save(existing);

		if (updated.getProjet() != null) {
			updateProjetStatus(updated.getProjet().getNumeroProjet());
		}

		return toDTO(updated);
	}

	public List<TacheDTO> getTachesByProjet(Long projetId) {
		return tacheRepository.findByProjet_NumeroProjetAndArchivedFalse(projetId).stream().map(this::toDTO)
				.collect(Collectors.toList());
	}

	public List<TacheDTO> getArchivedTachesByProjet(Long projetId) {
		return tacheRepository.findByProjetNumeroProjetAndArchivedTrue(projetId).stream().map(this::toDTO)
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public double calculerTauxBlocage(Long projetId) {
		List<Tache> taches = tacheRepository.findByProjet_NumeroProjetAndArchivedFalse(projetId);

		if (taches.isEmpty()) {
			return 0.0; // Pas de tâches, taux = 0
		}

		long nbBloquees = taches.stream().filter(t -> t.getEtat() == E_EtatTache.BLOQUE).count();

		return ((double) nbBloquees / taches.size()) * 100;
	}

	@Transactional
	public TacheDTO updateTacheFromDTO(Long id, TacheDTO dto) {
	    Tache existing = tacheRepository.findById(id)
	            .orElseThrow(() -> new EntityNotFoundException("Tâche non trouvée avec l'ID : " + id));

	    if (dto.getTitre() != null) existing.setTitre(dto.getTitre());
	    if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
	    if (dto.getEtat() != null) existing.setEtat(E_EtatTache.valueOf(dto.getEtat()));
	    if (dto.getPriorite() != null) existing.setPriorite(E_PrioriteTache.valueOf(dto.getPriorite()));
	    if (dto.getDateFin() != null) existing.setDateFin(dto.getDateFin());

	    // Ancien assignee principal
	    User ancienAssignee = existing.getAssigneeA();

	    // Mise à jour de l’assignee principal
	    if (dto.getAssigneeMatricule() != null) {
	        User newAssignee = userRepository.findByMatricule(dto.getAssigneeMatricule())
	                .orElseThrow(() -> new EntityNotFoundException(
	                        "Utilisateur introuvable : " + dto.getAssigneeMatricule()
	                ));
	        existing.setAssigneeA(newAssignee);

	        // Notifier uniquement si le nouveau assignee est différent
	        if (!newAssignee.equals(ancienAssignee)) {
	            notificationService.creerNotification(
	                    newAssignee,
	                    "Vous avez été assigné à la tâche : " + existing.getTitre(),
	                    TypeNotification.TACHE_ASSIGNATION,
	                    existing.getProjet(),
	                    existing
	            );
	        }
	    }

	    // Ancienne liste des autres assignés
	    Set<User> anciensAutres = new HashSet<>(existing.getAutresAssignes());

	    // Mise à jour des autres assignés
	    if (dto.getAutresAssignes() != null) {
	        List<User> autres = dto.getAutresAssignes().stream()
	                .map(member -> userRepository.findByMatricule(member.getMatricule())
	                        .orElseThrow(() -> new EntityNotFoundException(
	                                "Utilisateur introuvable : " + member.getMatricule()
	                        )))
	                .collect(Collectors.toList());
	        existing.setAutresAssignes(autres);

	        // Identifier les nouveaux autres assignés
	        Set<User> nouveauxAutres = new HashSet<>(autres);
	        nouveauxAutres.removeAll(anciensAutres);

	        // Notifier les nouveaux autres assignés
	        for (User nouveau : nouveauxAutres) {
	            notificationService.creerNotification(
	                    nouveau,
	                    "Vous avez été ajouté comme membre sur la tâche : " + existing.getTitre(),
	                    TypeNotification.TACHE_ASSIGNATION,
	                    existing.getProjet(),
	                    existing
	            );
	        }
	    }

	    Tache updated = tacheRepository.save(existing);
	    return toDTO(updated);
	}


	
	//our afficher les taches eli teb3in chaque memeber fel dahsboard mte3ou 
	@Transactional(readOnly = true)
	public List<TacheDTO> getTachesForMember(String matricule) {
	    User user = userRepository.findByMatricule(matricule)
	            .orElseThrow(() -> new RuntimeException("Utilisateur introuvable : " + matricule));

	    if (user.getRole() != ERole.MEMBER) {
	        return Collections.emptyList();
	    }

	    return tacheRepository.findAll().stream()
	            .filter(t -> !t.isArchived() &&
	                        (user.equals(t.getAssigneeA()) || t.getAutresAssignes().contains(user)))
	            .map(this::toDTO)
	            .collect(Collectors.toList());
	}
	
	
	private boolean hasAuthority(String authority) {
	    org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth == null) return false;
	    return auth.getAuthorities().stream()
	            .map(GrantedAuthority::getAuthority)
	            .anyMatch(a -> a.equalsIgnoreCase(authority));
	}

	private String getCurrentMatricule() {
	    org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth == null) throw new RuntimeException("Utilisateur non authentifié");
	    return auth.getName(); // chez vous, name = matricule
	}

	@Transactional(readOnly = true)
	public List<TacheDTO> getMyTachesDTO() {
	    String matricule = getCurrentMatricule();
	    return tacheRepository.findActiveByAnyAssignment(matricule)
	            .stream().map(this::toDTO).toList();
	}

	/** Option : /api/taches renvoie uniquement ses tâches si MEMBER, sinon tout pour ADMIN/MANAGER */
	@Transactional(readOnly = true)
	public List<TacheDTO> getTachesRespectingRole() {
	    if (hasAuthority("MEMBER")) {
	        return getMyTachesDTO();
	    }
	    return tacheRepository.findAll().stream().map(this::toDTO).toList();
	}


}
