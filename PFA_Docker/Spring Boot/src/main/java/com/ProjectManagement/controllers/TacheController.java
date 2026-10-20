package com.ProjectManagement.controllers;

import com.ProjectManagement.DTO.*;
import com.ProjectManagement.entities.*;
import com.ProjectManagement.services.*;

import jakarta.persistence.EntityNotFoundException;

import com.ProjectManagement.repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;



@RestController
@RequestMapping("/api/taches")
@CrossOrigin("*")
public class TacheController {

    private final TacheService tacheService;
  @Autowired TacheRepository tacheRepository;
    public TacheController(TacheService tacheService) {
        this.tacheService = tacheService;
    }

    // --- Lister toutes les tâches ---
    @GetMapping
    public List<TacheDTO> getAllTaches() {
        return tacheService.getAllTachesDTO();
    }

    // --- Récupérer une tâche par ID ---
 // --- Récupérer une tâche par ID ---
    @GetMapping("/{id}")
    public ResponseEntity<TacheDTO> getTacheById(@PathVariable Long id) {
        Tache tache = tacheRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tâche introuvable"));
        return ResponseEntity.ok(tacheService.toDTO(tache));
    }


    // --- Créer une nouvelle tâche ---
   
    @PostMapping
    public ResponseEntity<TacheDTO> createTache(@RequestBody TacheDTO dto) {
        System.out.println("Requête POST reçue : " + dto);
        System.out.println("AssigneeMatricule : " + dto.getAssigneeMatricule());
        return ResponseEntity.ok(tacheService.createTacheFromDTO(dto));
    }

    // --- Mettre à jour une tâche ---
    @PutMapping("/{id}")
    public ResponseEntity<TacheDTO> updateTache(@PathVariable Long id, @RequestBody TacheDTO dto) {
        return ResponseEntity.ok(tacheService.updateTacheFromDTO(id, dto));
    }

    // --- archiver une tâche ---
    @PatchMapping("/{id}/archive")
    public ResponseEntity<Void> archiveTache(@PathVariable Long id) {
        tacheService.archiveTache(id);
        return ResponseEntity.noContent().build();
    }

    
    @GetMapping("/byProjet/{projetId}/archived")
    public List<TacheDTO> getArchivedTachesByProjet(@PathVariable Long projetId) {
        return tacheService.getArchivedTachesByProjet(projetId);
    }


   
    @PatchMapping("/{id}/etat")
    public ResponseEntity<TacheDTO> updateEtat(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String nouvelEtat = payload.get("etat");
        return ResponseEntity.ok(tacheService.updateEtat(id, nouvelEtat));
    }
    
    @GetMapping("/byProjet/{projetId}")
    public List<TacheDTO> getTachesByProjet(@PathVariable Long projetId) {
        return tacheService.getTachesByProjet(projetId);
    }

    @GetMapping("/byProjet/{projetId}/tauxBlocage")
    public ResponseEntity<Map<String, Object>> getTauxBlocage(@PathVariable Long projetId) {
        double taux = tacheService.calculerTauxBlocage(projetId);
        return ResponseEntity.ok(Map.of("tauxBlocage", taux));
    }
    
    @GetMapping("/byMember/{matricule}")
    public List<TacheDTO> getTachesByMember(@PathVariable String matricule) {
        return tacheService.getTachesForMember(matricule);
    }
    
    @GetMapping("/all")
    public List<TacheDTO> getAllTachesForMember() {
        return tacheService.getTachesRespectingRole();
    }
    @GetMapping("/me")
    public List<TacheDTO> getMyTaches() {
        return tacheService.getMyTachesDTO();
    }





}