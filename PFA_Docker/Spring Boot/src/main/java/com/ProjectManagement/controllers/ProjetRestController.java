package com.ProjectManagement.controllers;

import com.ProjectManagement.DTO.ProjetDTO;
import com.ProjectManagement.entities.Projet;
import com.ProjectManagement.services.ProjetService;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.*;

@RestController
@RequestMapping("/api/projets")
@CrossOrigin("*")
public class ProjetRestController {

	private final ProjetService projetService;
	private final Path uploadDir = Paths.get("src/main/resources/static/uploads/files");

	@Autowired
	public ProjetRestController(ProjetService projetService) {
		this.projetService = projetService;
	}

	// ====================================================================================
	// 1️⃣ RECHERCHE AVANCÉE DE PROJETS (multi-filtres + pagination + tri)
	// ====================================================================================
	@Operation(summary = "Recherche avancée de projets (multi-critères)", description = """
			Endpoint majeur permettant de filtrer les projets selon de multiples paramètres.

			✔ Filtres :
			- q (mot-clé global)
			- titre
			- description
			- status (EN_COURS, TERMINE, RETARD, ARCHIVE, etc.)
			- dateDebutFrom / dateDebutTo
			- dateFinFrom / dateFinTo
			- hasFiles (projets avec pièces jointes)
			- archived (true/false)
			- chefMatricule
			- membreMatricule

			✔ Pagination :
			- page (def=0)
			- size (def=5)

			✔ Tri :
			- sortBy (def=dateDebut)
			- sortDir (asc/desc)

			🔎 **Tests QA recommandés :**
			- Combinaisons multiples de filtres
			- Filtres vides → résultats cohérents
			- Vérifier tri multi-champs → dateDebut puis dateFin
			- Vérifier pagination (page 0, page 1…)
			- Tester performances avec >200 projets
			""")
	@ApiResponse(responseCode = "200", description = "Résultats renvoyés", content = @Content(schema = @Schema(implementation = ProjetDTO.class)))
	@GetMapping("/search")
	public ResponseEntity<Page<ProjetDTO>> search(@RequestParam(required = false) String q,
			@RequestParam(required = false) String titre, @RequestParam(required = false) String description,
			@RequestParam(required = false) String status,

			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebutFrom,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebutTo,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFinFrom,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFinTo,

			@RequestParam(required = false) Boolean hasFiles, @RequestParam(required = false) Boolean archived,
			@RequestParam(required = false) String chefMatricule,
			@RequestParam(required = false) String membreMatricule,

			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = "dateDebut") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDir) {
		Sort sortPrimary = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending()
				: Sort.by(sortBy).ascending();

		// Tri secondaire stable pour cohérence d’affichage
		Sort sort = sortPrimary.and(Sort.by("dateFin").ascending());

		PageRequest pageable = PageRequest.of(page, size, sort);

		Page<ProjetDTO> result = projetService.search(q, titre, description, status, dateDebutFrom, dateDebutTo,
				dateFinFrom, dateFinTo, hasFiles, archived, chefMatricule, membreMatricule, pageable);

		return ResponseEntity.ok(result);
	}

	// ====================================================================================
	// GET ALL PROJECTS
	// ====================================================================================
	@Operation(summary = "Lister tous les projets", description = """
			Retourne la liste complète des projets.

			🔎 **Tests QA :**
			- Large volume (100+ projets)
			- Cohérence tri backend (selon service)
			""")
	@GetMapping
	public ResponseEntity<List<ProjetDTO>> getAllProjets() {
		return ResponseEntity.ok(projetService.getAllProjetsDTO());
	}

	// ====================================================================================
	// GET BY NUMERO PROJET
	// ====================================================================================
	@Operation(summary = "Obtenir un projet via son numéro", description = """
			Retourne le projet correspondant via numeroProjet.

			🔎 Tests QA :
			- Projet existant → 200
			- Projet inexistant → 404
			""")
	@GetMapping("/{numeroProjet}")
	public ResponseEntity<ProjetDTO> getProjetByNumeroProjet(@PathVariable Long numeroProjet) {
		try {
			return ResponseEntity.ok(projetService.getProjetDTOByNumero(numeroProjet));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	// ====================================================================================
	// CREATE PROJECT (multipart + fichiers)
	// ====================================================================================
	@Operation(summary = "Créer un projet", description = """
			Création d’un projet complet avec :
			✔ Chef de projet
			✔ Membres
			✔ Dates
			✔ Statut
			✔ Upload fichiers (PDF, DOCX, XLSX…)

			⚠ Endpoint multipart → nécessite Postman ou Swagger compatible.
			""")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "Projet créé"),
			@ApiResponse(responseCode = "400", description = "Erreur upload ou données invalides") })
	@PostMapping("/create/{matriculeChef}")
	public ResponseEntity<?> createProjet(@PathVariable String matriculeChef, @RequestParam("titre") String titre,
			@RequestParam("description") String description,
			@RequestParam(value = "dateDebut", required = false) String dateDebut,
			@RequestParam(value = "dateFin", required = false) String dateFin, @RequestParam("status") String status,
			@RequestParam(value = "membres", required = false) List<String> membres,
			@RequestPart(value = "fichiers", required = false) List<MultipartFile> fichiers) {

		try {
			LocalDate debut = (dateDebut != null && !dateDebut.isEmpty()) ? LocalDate.parse(dateDebut) : null;
			LocalDate fin = (dateFin != null && !dateFin.isEmpty()) ? LocalDate.parse(dateFin) : null;

			Projet projet = projetService.creerProjet(titre, description, debut, fin, status, membres, matriculeChef,
					fichiers);

			// Retourner un DTO pour éviter les problèmes de serialization (Lazy)
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(projetService.getProjetDTOByNumero(projet.getNumeroProjet()));
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur upload : " + e.getMessage());
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
		}
	}

	// ====================================================================================
	// UPDATE PROJECT (multipart + removedFiles)
	// ====================================================================================
	@Operation(summary = "Mettre à jour un projet", description = """
			Met à jour un projet existant :
			✔ Métadonnées
			✔ Membres
			✔ Dates
			✔ Fichiers uploadés
			✔ Fichiers supprimés via removedFiles

			🔎 Tests QA :
			- Update avec ajout fichiers
			- Update avec suppression fichiers
			- Update dates invalides → error
			""")
	@PutMapping("/update/{numeroProjet}")
	public ResponseEntity<?> updateProjet(@PathVariable Long numeroProjet, @RequestParam("titre") String titre,
			@RequestParam("description") String description,
			@RequestParam(value = "dateDebut", required = false) String dateDebut,
			@RequestParam(value = "dateFin", required = false) String dateFin, @RequestParam("status") String status,
			@RequestParam(value = "membres", required = false) List<String> membres,
			@RequestPart(value = "fichiers", required = false) List<MultipartFile> fichiers,
			// ⬇⬇⬇ NEW: chemins/URLs stockés en base à retirer
			@RequestParam(value = "removedFiles", required = false) List<String> removedFiles) {
		try {
			LocalDate debut = (dateDebut != null && !dateDebut.isEmpty()) ? LocalDate.parse(dateDebut) : null;
			LocalDate fin = (dateFin != null && !dateFin.isEmpty()) ? LocalDate.parse(dateFin) : null;

			Projet projet = projetService.updateProjet(numeroProjet, titre, description, debut, fin, status, membres,
					fichiers, removedFiles // ⬅ add
			);

			return ResponseEntity.ok(projetService.getProjetDTOByNumero(projet.getNumeroProjet()));
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur upload : " + e.getMessage());
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
		}
	}

	// ====================================================================================
	// FILE DOWNLOAD
	// ====================================================================================
	@Operation(summary = "Télécharger un fichier du projet", description = """
			Télécharge un fichier attaché à un projet.

			🔎 QA :
			- Fichier existant → 200 + bon contentType
			- Fichier inexistant → 404
			- Tester PDF, images, ZIP…
			""")
	@GetMapping("/download/{filename:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
		try {
			Path filePath = uploadDir.resolve(filename).normalize();
			Resource resource = new UrlResource(filePath.toUri());

			if (!resource.exists() || !resource.isReadable()) {
				return ResponseEntity.notFound().build();
			}

			String contentType = Files.probeContentType(filePath);
			if (contentType == null) {
				contentType = "application/octet-stream";
			}

			return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
					.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
					.body(resource);

		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// ====================================================================================
	// PAGINATION DES PROJETS
	// ====================================================================================
	@Operation(summary = "Pagination simple des projets", description = """
			Retourne une page de projets triés par dateDebut puis dateFin.

			🔎 Tests QA :
			- Vérifier pagination (page 0 → première page)
			- Vérifier size dynamique
			- Vérifier ordre ascendant des dates
			""")
	@GetMapping("/pagination")
	public ResponseEntity<Page<ProjetDTO>> getProjetsWithPagination(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {

		// Ajout du tri : d'abord par dateDebut, puis par dateFin
		PageRequest pageable = PageRequest.of(page, size,
				Sort.by("dateDebut").ascending().and(Sort.by("dateFin").ascending()));

		Page<ProjetDTO> projets = projetService.getAllProjetsPage(pageable);
		return ResponseEntity.ok(projets);
	}

//====================================================================================
//ARCHIVER UN PROJET
//====================================================================================
	@Operation(summary = "Archiver un projet", description = """
			Permet d'archiver un projet existant.

			🔎 Tests QA :
			- Projet existant → doit passer en archived = true
			- Projet inexistant → 404
			- Vérifier impact dans /archived et /search?archived=true
			""")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Projet archivé"),
			@ApiResponse(responseCode = "404", description = "Projet introuvable") })
	@PutMapping("/archive/{numeroProjet}")
	public ResponseEntity<?> archiveProjet(@PathVariable Long numeroProjet) {
		try {
			Projet projet = projetService.archiveProjet(numeroProjet);
			return ResponseEntity.ok(projetService.getProjetDTOByNumero(projet.getNumeroProjet()));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

//====================================================================================
//LISTE DES PROJETS ARCHIVÉS
//====================================================================================
	@Operation(summary = "Lister tous les projets archivés", description = """
			Retourne la liste complète des projets archivés.

			🔎 Tests QA :
			- Vérifier cohérence après archivage
			- Vérifier visibilité uniquement archived
			""")
	@GetMapping("/archived")
	public ResponseEntity<List<ProjetDTO>> getArchivedProjets() {
		return ResponseEntity.ok(projetService.getArchivedProjects());
	}

//====================================================================================
//PAGINATION ARCHIVES
//====================================================================================
	@Operation(summary = "Pagination des projets archivés", description = """
			Retourne une page de projets archivés.

			🔎 Tests QA :
			- Vérifier pagination
			- Vérifier uniquement archived = true
			""")
	@GetMapping("/archived/pagination")
	public ResponseEntity<Page<ProjetDTO>> getArchivedProjetsWithPagination(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {

		Page<ProjetDTO> projets = projetService.getArchivedProjectsPage(PageRequest.of(page, size));
		return ResponseEntity.ok(projets);
	}

//====================================================================================
//STATS PAR PROJET
//====================================================================================
	@Operation(summary = "Statistiques d’un projet", description = """
			Retourne des données analytiques pour un projet :
			✔ tâches par statut
			✔ avancement
			✔ charge
			✔ nombre de membres
			✔ progression temps réel

			🔎 Tests QA :
			- Projet inexistant → 404
			- Cohérence des chiffres avec les données réelles
			""")
	@GetMapping("/{numeroProjet}/stats")
	public ResponseEntity<?> getProjetStats(@PathVariable Long numeroProjet) {
		try {
			return ResponseEntity.ok(projetService.getProjetStats(numeroProjet));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

//====================================================================================
//STATS GLOBALES
//====================================================================================
	@Operation(summary = "Statistiques globales des projets", description = """
			Fournit un résumé global du portefeuille projets :
			✔ Nombre total de projets
			✔ Nombre de projets actifs / terminés / en retard
			✔ Taux d’achèvement global

			🔎 Tests QA :
			- Vérifier exactitude par rapport à la DB
			- Tester sur grande volumétrie
			""")
	@GetMapping("/global-stats")
	public ResponseEntity<Map<String, Long>> getGlobalProjetStats() {
		return ResponseEntity.ok(projetService.getGlobalProjetStats());
	}

//====================================================================================
//EXPORT PDF SÉCURISÉ
//====================================================================================
	@Operation(summary = "Générer un PDF sécurisé", description = """
			Reçoit un PDF en base64 → retourne un PDF chiffré AES-128.

			✔ Mot de passe utilisateur : 1234
			✔ Mot de passe admin : adminPass
			✔ Permissions limitées (impression uniquement)

			🔎 Tests QA :
			- Télécharger le PDF généré
			- Vérifier impossibilité modification
			- Vérifier mot de passe utilisateur
			""")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "PDF sécurisé généré"), })
	@PostMapping("/secure-pdf")
	public ResponseEntity<byte[]> securePdf(@RequestBody Map<String, String> request) throws IOException {
		String base64Pdf = request.get("pdfBase64");
		byte[] pdfBytes = java.util.Base64.getDecoder().decode(base64Pdf);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfReader reader = new PdfReader(new java.io.ByteArrayInputStream(pdfBytes));
		WriterProperties writerProperties = new WriterProperties().setStandardEncryption("1234".getBytes(),
				"adminPass".getBytes(), EncryptionConstants.ALLOW_PRINTING, EncryptionConstants.ENCRYPTION_AES_128);
		PdfWriter writer = new PdfWriter(baos, writerProperties);

		PdfDocument pdfDoc = new PdfDocument(reader, writer);
		pdfDoc.close();

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Rapport_Secure.pdf")
				.contentType(MediaType.APPLICATION_PDF).body(baos.toByteArray());
	}

	// ====================================================================================
	// COMPARAISON PROJETS
	// ====================================================================================
	@Operation(summary = "Comparer l’avancement des projets", description = """
			Donne une vue comparative :
			✔ avancée
			✔ deadlines
			✔ charge
			✔ complexité

			🔎 Tests QA :
			- Vérifier classement
			- Vérifier cohérence chiffres
			""")
	@GetMapping("/comparaison")
	public ResponseEntity<List<Map<String, Object>>> getComparaisonProjets() {
		return ResponseEntity.ok(projetService.getComparaisonProjets());
	}

//====================================================================================
//DEADLINES PROCHES
//====================================================================================
	@Operation(summary = "Lister les projets dont les deadlines approchent", description = """
			Retourne les projets qui arrivent à échéance.

			🔎 Tests QA :
			- Vérifier range par défaut 10 jours
			- Tester days=1, 3, 7, 30
			""")
	@GetMapping("/upcoming-deadlines")
	public ResponseEntity<List<Map<String, Object>>> getUpcomingDeadlines() {
		return ResponseEntity.ok(projetService.getUpcomingDeadlines());
	}

	@GetMapping("/projects-by-chef-status")
	public ResponseEntity<Map<String, Map<String, Long>>> getProjectsByChefAndStatus() {
		return ResponseEntity.ok(projetService.getProjectsByChefAndStatus());
	}

	// ====================================================================================
	// GANTT VIEW
	// ====================================================================================
	@Operation(summary = "Données format Gantt Chart", description = """
			Retourne les projets dans un format JSON compatible diagrammes Gantt.

			🔎 Tests QA :
			- Vérifier format obligatoire (start, end, title)
			""")
	@GetMapping("/gantt-projects")
	public ResponseEntity<List<Map<String, Object>>> getProjectsForGantt() {
		return ResponseEntity.ok(projetService.getProjectsForGantt());
	}

//====================================================================================
//PROJETS PAR MEMBRE
//====================================================================================
	@Operation(summary = "Lister les projets assignés à un membre", description = "Permet de retrouver les projets où un utilisateur est membre.")
	@GetMapping("/member/{matricule}")
	public ResponseEntity<List<ProjetDTO>> getProjetsByMember(@PathVariable String matricule) {
		return ResponseEntity.ok(projetService.getProjetsForMember(matricule));
	}

//====================================================================================
//RETOURNER UN NUMERO PROJET VIA ID
//====================================================================================
	@Operation(summary = "Récupérer le numéro projet via ID interne", description = "Utilisé par le front pour récupérer le numéro projet.")
	@GetMapping("/numero-by-id/{id}")
	public ResponseEntity<Long> getNumeroProjetById(@PathVariable Long id) {
		return ResponseEntity.ok(projetService.getNumeroProjetById(id));
	}

//====================================================================================
//PAGINATION PROJETS PAR CHEF
//====================================================================================
	@Operation(summary = "Pagination des projets où l’utilisateur est chef", description = """
			Permet au chef de voir ses projets paginés.

			🔎 Tests QA :
			- Vérifier tri : dateDebut + dateFin
			- Vérifier cohérence profil chef
			""")
	@GetMapping("/chef/{matricule}/pagination")
	public ResponseEntity<Page<ProjetDTO>> getProjetsForChefWithPagination(@PathVariable String matricule,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {

		PageRequest pageable = PageRequest.of(page, size,
				Sort.by("dateDebut").ascending().and(Sort.by("dateFin").ascending()));
		return ResponseEntity.ok(projetService.getProjetsPageForChef(matricule, pageable));
	}

//====================================================================================
//PAGINATION ARCHIVES POUR CHEF
//====================================================================================
	@Operation(summary = "Pagination des projets archivés pour un chef")
	@GetMapping("/chef/{matricule}/archived/pagination")
	public ResponseEntity<Page<ProjetDTO>> getArchivedProjetsForChefWithPagination(@PathVariable String matricule,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {

		PageRequest pageable = PageRequest.of(page, size);
		return ResponseEntity.ok(projetService.getArchivedProjetsPageForChef(matricule, pageable));
	}

//====================================================================================
//COMPARAISON PROJETS PAR CHEF
//====================================================================================
	@Operation(summary = "Comparer les projets d’un chef")
	@GetMapping("/chef/{matricule}/comparaison")
	public ResponseEntity<List<Map<String, Object>>> getComparaisonProjetsForChef(@PathVariable String matricule) {
		return ResponseEntity.ok(projetService.getComparaisonProjetsForChef(matricule));
	}

//====================================================================================
// DEADLINES POUR CHEF
//====================================================================================
	@Operation(summary = "Deadlines proches pour les projets d’un chef")
	@GetMapping("/chef/{matricule}/upcoming-deadlines")
	public ResponseEntity<List<Map<String, Object>>> getUpcomingDeadlinesForChef(@PathVariable String matricule,
			@RequestParam(defaultValue = "10") int days) {
		return ResponseEntity.ok(projetService.getUpcomingDeadlinesForChef(matricule, days));
	}

}