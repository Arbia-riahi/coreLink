package com.ProjectManagement.controllers;

import com.ProjectManagement.entities.Actualite;
import com.ProjectManagement.repositories.ActualiteRepository;
import com.ProjectManagement.services.ActualiteService;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/actualites")
@CrossOrigin("*")
public class ActualiteRestController {

	private final ActualiteService actualiteService;
	private final ActualiteRepository repository;

	@Autowired
	public ActualiteRestController(ActualiteService actualiteService, ActualiteRepository repository) {
		this.actualiteService = actualiteService;
		this.repository = repository;
	}

	// ============================================================
	// LISTER LES ACTUALITÉS (PAGINÉ, TRI RÉCENT → ANCIEN)
	// ============================================================
	@Operation(summary = "Lister les actualités paginées", description = "Retourne la liste paginée des actualités (5 élements per page) , triées du plus récent au plus ancien.")
	@ApiResponse(responseCode = "200", description = "Liste paginée renvoyée avec succès")
	@GetMapping
	public Page<Actualite> listPaged(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("dateCreation").descending());
		return repository.findAll(pageable);
	}

	// ============================================================
	// ️RÉCUPÉRER UNE ACTUALITÉ PAR ID
	// ============================================================
	@Operation(summary = "Obtenir une actualité par ID", description = """
			Retourne une actualité spécifique.

			🔎 **Tests API recommandés :**
			- ID valide → 200 OK
			- ID inexistant → lève une exception au niveau service (tester comportement)
			- Vérifier structure JSON du retour

			Format attendu ID : Long
			""")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Actualité trouvée"),
			@ApiResponse(responseCode = "404", description = "Aucune actualité trouvée pour cet ID") })
	@GetMapping("/{id}")
	public ResponseEntity<Actualite> getActualiteById(@PathVariable Long id) {
		Actualite actu = actualiteService.findById(id);
		return ResponseEntity.ok(actu);
	}

	// ============================================================
	// CRÉER UNE ACTUALITÉ
	// ============================================================
	@Operation(summary = "Créer une nouvelle actualité", description = """
			Permet de créer une actualité avec un titre, une description et une ou plusieurs images.

			✔ **Type : multipart/form-data**
			✔ **Champs obligatoires :**
			  - titre : String
			  - description : String
			  - imageFiles : List<MultipartFile>

			🔎 **Tests QA importants :**
			- Upload multi-images
			- Images manquantes
			- Titre vide → erreur attendue ?
			- Description longue
			- Fichiers invalides (PDF, EXE)
			- Poids max des fichiers

			""")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "Actualité créée avec succès"),
			@ApiResponse(responseCode = "400", description = "Données invalides") })
	@PostMapping(consumes = "multipart/form-data")
	public ResponseEntity<Actualite> create(@RequestParam("titre") String titre,
			@RequestParam("description") String description, @RequestParam("imageFiles") List<MultipartFile> imageFiles)
			throws IOException {
		Actualite created = actualiteService.create(imageFiles, titre, description);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	// ============================================================
	// SUPPRIMER UNE ACTUALITÉ
	// ============================================================
	@Operation(summary = "Supprimer une actualité", description = """
			Supprime une actualité existante.

			🔎 **Points importants pour QA :**
			- ID valide → 204 No Content
			- ID inexistant → 404 Not Found
			- Vérifier qu'aucune donnée résiduelle ne reste
			- Vérifier suppression des images associées (si applicable)
			""")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Suppression réussie"),
			@ApiResponse(responseCode = "404", description = "Actualité introuvable") })
	@DeleteMapping("/{actuId}")
	public ResponseEntity<Void> deleteActualite(@PathVariable Long actuId) {
		try {
			actualiteService.delete(actuId);
			return ResponseEntity.noContent().build();
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	// ============================================================
	// 5️⃣ METTRE À JOUR UNE ACTUALITÉ
	// ============================================================
	@Operation(summary = "Mettre à jour une actualité existante", description = """
			Met à jour le titre, la description et éventuellement les images d'une actualité.

			✔ Type : multipart/form-data
			✔ Les images sont facultatives

			🔎 **Tests API recommandés :**
			- Mise à jour sans images
			- Mise à jour avec ajout de nouvelles images
			- ID inexistant → 404
			- Validation sur titre/description
			""")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Actualité mise à jour"),
			@ApiResponse(responseCode = "404", description = "Actualité introuvable") })
	@PutMapping(value = "/{id}", consumes = "multipart/form-data")
	public ResponseEntity<Actualite> updateActualite(@PathVariable("id") Long id, @RequestParam("titre") String titre,
			@RequestParam("description") String description,
			@RequestParam(value = "imageFiles", required = false) List<MultipartFile> imageFiles) throws IOException {
		Actualite updated = actualiteService.update(id, titre, description, imageFiles);
		return ResponseEntity.ok(updated);
	}
}
