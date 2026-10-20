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

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/actualites")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ActualiteRestController {

    private final ActualiteService actualiteService;
    private final ActualiteRepository repository;

    @Autowired
    public ActualiteRestController(ActualiteService actualiteService, ActualiteRepository repository) {
        this.actualiteService = actualiteService;
        this.repository = repository;
    }

    /* =============================================================
                        GET PAGINÉ — LISTE AVEC IMAGES
       ============================================================= */
    @GetMapping
    public Page<Actualite> listPaged(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dateCreation").descending());
        Page<Actualite> pageResult = repository.findAll(pageable);

        // ⭐ OBLIGATOIRE : construire les URLs complètes /uploads/files/xxx.png
        pageResult.forEach(actualiteService::buildImageUrls);

        return pageResult;
    }

    /* =============================================================
                         GET BY ID (avec images)
       ============================================================= */
    @GetMapping("/{id}")
    public ResponseEntity<Actualite> getActualiteById(@PathVariable Long id) {
        Actualite actu = actualiteService.findById(id);
        return ResponseEntity.ok(actualiteService.buildImageUrls(actu));
    }

    /* =============================================================
                              CREATE ACTUALITE
       ============================================================= */
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Actualite> create(
            @RequestParam("titre") String titre,
            @RequestParam("description") String description,
            @RequestParam("imageFiles") List<MultipartFile> imageFiles
    ) throws IOException {

        Actualite created = actualiteService.create(imageFiles, titre, description);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /* =============================================================
                              DELETE ACTUALITE
       ============================================================= */
    @DeleteMapping("/{actuId}")
    public ResponseEntity<Void> deleteActualite(@PathVariable Long actuId) {
        try {
            actualiteService.delete(actuId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /* =============================================================
                              UPDATE ACTUALITE
       ============================================================= */
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Actualite> updateActualite(
            @PathVariable("id") Long id,
            @RequestParam("titre") String titre,
            @RequestParam("description") String description,
            @RequestParam(value = "imageFiles", required = false) List<MultipartFile> imageFiles
    ) throws IOException {

        Actualite updated = actualiteService.update(id, titre, description, imageFiles);
        return ResponseEntity.ok(updated);
    }
}
