package com.ProjectManagement.services;

import com.ProjectManagement.DTO.MemberDTO;
import com.ProjectManagement.DTO.ProjetDTO;
import com.ProjectManagement.entities.*;
import com.ProjectManagement.repositories.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.data.jpa.domain.Specification;
import static com.ProjectManagement.repositories.ProjetSpecifications.*;



@Service
public class ProjetService {

    private final ProjetRepository projetRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final TacheRepository tacheRepository;

    @Value("${file.upload-dir:src/main/resources/static/uploads/files}")
    private String uploadDir;

    public ProjetService(ProjetRepository projetRepository,
                         UserRepository userRepository,
                         TacheRepository tacheRepository,
                         NotificationService notificationService) {
        this.projetRepository = projetRepository;
        this.userRepository = userRepository;
        this.tacheRepository = tacheRepository;
        this.notificationService = notificationService;
    }
    
    @Transactional(readOnly = true)
    public Page<ProjetDTO> search(
            String q,
            String titre,
            String description,
            String status,
            LocalDate dateDebutFrom,
            LocalDate dateDebutTo,
            LocalDate dateFinFrom,
            LocalDate dateFinTo,
            Boolean hasFiles,
            Boolean archived,
            String chefMatricule,
            String membreMatricule,
            Pageable pageable
    ) {
        Specification<Projet> spec = Specification.where(archived(archived))
                .and(q(q))
                .and(titreContains(titre))
                .and(descriptionContains(description))
                .and(statusEquals(status))
                .and(dateDebutBetween(dateDebutFrom, dateDebutTo))
                .and(dateFinBetween(dateFinFrom, dateFinTo))
                .and(hasFiles(hasFiles))
                .and(chefMatriculeEquals(chefMatricule))
                .and(membreMatriculeEquals(membreMatricule));

        return projetRepository.findAll(spec, pageable).map(this::mapToDTO);
    }


    // --- Lister tous les projets ---
    @Transactional(readOnly = true)
    public List<ProjetDTO> getAllProjetsDTO() {
        return projetRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProjetDTO getProjetDTOByNumero(Long numeroProjet) {
        Projet projet = projetRepository.findById(numeroProjet)
                .orElseThrow(() -> new RuntimeException("Projet introuvable."));
        return mapToDTO(projet);
    }

    // --- Créer un projet avec notification pour les membres ---
    @Transactional
    public Projet creerProjet(String titre, String description, LocalDate dateDebut, LocalDate dateFin,
                              String status, List<String> membres, String matriculeChef,
                              List<MultipartFile> fichiers) throws IOException {

        User chef = userRepository.findByMatricule(matriculeChef)
                .orElseThrow(() -> new RuntimeException("Chef de projet introuvable."));

        Projet projet = new Projet();
        projet.setTitre(titre);
        projet.setDescription(description);
        projet.setDateDebut(dateDebut);
        projet.setDateFin(dateFin);
        projet.setStatus(EProjet.valueOf(status));
        projet.setChefProjet(chef);

        // Ajout des membres
        if (membres != null) {
            for (String matricule : membres) {
                userRepository.findByMatricule(matricule)
                        .ifPresent(projet.getMembres()::add);
            }
        }

        // Ajout des fichiers
        if (fichiers != null && !fichiers.isEmpty()) {
            for (MultipartFile file : fichiers) {
                String nomFichier = saveFile(file);
                if (nomFichier != null) projet.getFichiers().add(nomFichier);
            }
        }

        Projet saved = projetRepository.save(projet);

        // Notifier uniquement les membres
        notifierMembresProjet(saved, chef);

        return saved;
    }

 // ProjetService.java

    @Transactional
    public Projet updateProjet(Long numeroProjet, String titre, String description,
                               LocalDate dateDebut, LocalDate dateFin, String status,
                               List<String> membres, List<MultipartFile> fichiers,
                               List<String> removedFiles) throws IOException {  // ⬅ add
        Projet projet = projetRepository.findById(numeroProjet)
                .orElseThrow(() -> new RuntimeException("Projet introuvable."));

        if (titre != null && !titre.isBlank()) projet.setTitre(titre);
        if (description != null)               projet.setDescription(description);
        if (dateDebut != null)                 projet.setDateDebut(dateDebut);
        if (dateFin != null)                   projet.setDateFin(dateFin);
        if (status != null)                    projet.setStatus(EProjet.valueOf(status));

        // --- Membres (inchangé) ---
        Set<User> anciensMembres = new HashSet<>(projet.getMembres());
        projet.getMembres().clear();
        if (membres != null) {
            for (String matricule : membres) {
                userRepository.findByMatricule(matricule)
                        .ifPresent(projet.getMembres()::add);
            }
        }
        Set<User> nouveauxMembres = new HashSet<>(projet.getMembres());
        nouveauxMembres.removeAll(anciensMembres);
        for (User nouveau : nouveauxMembres) {
            if (!nouveau.equals(projet.getChefProjet())) {
                notificationService.creerNotification(
                        nouveau,
                        "Vous avez été ajouté au projet : " + projet.getTitre(),
                        TypeNotification.AFFECTATION_MEMBRE_PROJET,
                        projet,
                        null
                );
            }
        }

        // --- SUPPRESSION des fichiers marqués ---
        if (removedFiles != null && !removedFiles.isEmpty()) {
            removeFilesFromProject(projet, removedFiles);
        }

        // --- Ajout de nouveaux fichiers (inchangé) ---
        if (fichiers != null && !fichiers.isEmpty()) {
            for (MultipartFile file : fichiers) {
                String nomFichier = saveFile(file);
                if (nomFichier != null) projet.getFichiers().add(nomFichier);
            }
        }

        return projetRepository.save(projet);
    }


    // --- Archiver un projet ---
    @Transactional
    public Projet archiveProjet(Long numeroProjet) {
        Projet projet = projetRepository.findById(numeroProjet)
                .orElseThrow(() -> new RuntimeException("Projet introuvable."));
        projet.setArchived(true);
        return projetRepository.save(projet);
    }

    // --- Projets archivés ---
    @Transactional(readOnly = true)
    public List<ProjetDTO> getArchivedProjects() {
        return projetRepository.findByArchivedTrue()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // --- DTO ---
    private ProjetDTO mapToDTO(Projet projet) {
        String chefNom = projet.getChefProjet().getNom() + " " + projet.getChefProjet().getPrenom();
        List<MemberDTO> membres = projet.getMembres().stream()
                .map(u -> new MemberDTO(u.getMatricule(), u.getNom(), u.getPrenom(), u.getImage()))
                .collect(Collectors.toList());

        return new ProjetDTO(
                projet.getNumeroProjet(),
                projet.getTitre(),
                projet.getDescription(),
                projet.getDateDebut(),
                projet.getDateFin(),
                projet.getStatus() != null ? projet.getStatus().getLabel() : null,
                chefNom,
                membres,
                projet.getFichiers(),
                projet.isArchived()
        );
    }

    // --- Sauvegarde fichier ---
    private String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return null;

        Path root = Paths.get(uploadDir);
        if (!Files.exists(root)) Files.createDirectories(root);

        String uniqueName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path destination = root.resolve(uniqueName);

        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/files/" + uniqueName;
    }

    // --- Notifications pour les membres uniquement ---
    private void notifierMembresProjet(Projet projet, User chef) {
        notificationService.creerNotificationPourMembresProjet(
                projet,
                chef,
                "Vous avez été ajouté au projet : " + projet.getTitre(),
                TypeNotification.AFFECTATION_MEMBRE_PROJET
        );
    }
    
    

  

   @Transactional(readOnly = true)
    public Page<ProjetDTO> getAllProjetsPage(Pageable pageable) {
        return projetRepository.findByArchivedFalse(pageable).map(this::mapToDTO);
    }

    
    @Transactional(readOnly = true)
    public Page<ProjetDTO> getArchivedProjectsPage(Pageable pageable) {
        return projetRepository.findByArchivedTrue(pageable)
                               .map(this::mapToDTO);
    }
    
   

    @Transactional(readOnly = true)
    public Map<String, Object> getProjetStats(Long numeroProjet) {
        Projet projet = projetRepository.findById(numeroProjet)
                .orElseThrow(() -> new RuntimeException("Projet introuvable : " + numeroProjet));

        // Récupérer uniquement les tâches NON archivées
        List<Tache> taches = tacheRepository.findByProjet_NumeroProjetAndArchivedFalse(numeroProjet);

        int nbTotal = taches.size();

        // Compter uniquement les tâches terminées
        int nbTerminees = (int) taches.stream()
                .filter(t -> t.getEtat() == E_EtatTache.TERMINE)
                .count();

        double progression = nbTotal == 0 ? 0 : (nbTerminees * 100.0 / nbTotal);

        // Compter le nombre de tâches par état (hors archivées)
        Map<String, Long> nbParEtat = taches.stream()
                .collect(Collectors.groupingBy(t -> t.getEtat().name(), Collectors.counting()));

        Map<String, Object> stats = new HashMap<>();
        stats.put("nbTotal", nbTotal);
        stats.put("nbTerminees", nbTerminees);
        stats.put("progression", progression);
        stats.put("nbParEtat", nbParEtat);
        
        stats.putAll(getVelocityChart(numeroProjet));
        stats.putAll(getBurnDownChart(numeroProjet));

        return stats;
    }

    
    
    @Transactional(readOnly = true)
    public Map<String, Object> getVelocityChart(Long numeroProjet) {
        List<Tache> tachesTerminees = tacheRepository
                .findByProjet_NumeroProjetAndArchivedFalse(numeroProjet)
                .stream()
                .filter(t -> t.getEtat() == E_EtatTache.TERMINE && t.getDateFin() != null)
                .collect(Collectors.toList());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Groupement par intervalle (lundi - dimanche)
        Map<String, Long> velocityByWeek = tachesTerminees.stream()
                .collect(Collectors.groupingBy(t -> {
                    LocalDate dateFin = t.getDateFin().toLocalDate();
                    LocalDate startOfWeek = dateFin.with(DayOfWeek.MONDAY);
                    LocalDate endOfWeek = startOfWeek.plusDays(6);
                    return startOfWeek.format(formatter) + " - " + endOfWeek.format(formatter);
                }, Collectors.counting()));

        return Map.of("velocityByWeek", velocityByWeek);
    }

    
    @Transactional(readOnly = true)
    public Map<String, Object> getBurnDownChart(Long numeroProjet) {
        List<Tache> taches = tacheRepository.findByProjet_NumeroProjetAndArchivedFalse(numeroProjet);
        if (taches.isEmpty()) return Map.of("burnDown", Collections.emptyList());

        LocalDateTime minDate = taches.stream()
                .map(Tache::getDateCreation)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now());

        LocalDateTime maxDate = taches.stream()
                .map(Tache::getDateFin)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now());

        List<Map<String, Object>> burnDown = new ArrayList<>();
        LocalDateTime current = minDate;

        while (!current.isAfter(maxDate)) {
            final LocalDateTime currentDate = current; // variable effectively final pour le lambda

            long remaining = taches.stream()
                    .filter(t -> t.getEtat() != E_EtatTache.TERMINE &&
                                 (t.getDateCreation() == null || !t.getDateCreation().isAfter(currentDate)))
                    .count();

            burnDown.add(Map.of(
                    "date", current.toLocalDate(),
                    "remainingTasks", remaining
            ));

            current = current.plusDays(1);
        }


        return Map.of("burnDown", burnDown);
    }

    
    @Transactional(readOnly = true)
    public Map<String, Long> getGlobalProjetStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("nbEnAttente", projetRepository.countByStatus(EProjet.EN_ATTENTE));
        stats.put("nbEnCours", projetRepository.countByStatus(EProjet.EN_COURS));
        stats.put("nbTermines", projetRepository.countByStatus(EProjet.TERMINE));
        stats.put("nbAnnules", projetRepository.countByStatus(EProjet.ANNULE));
        return stats;
    }

    
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getComparaisonProjets() {
        List<Projet> projets = projetRepository.findAll();
        List<Map<String, Object>> comparaison = new ArrayList<>();

        for (Projet projet : projets) {
            List<Tache> taches = tacheRepository.findByProjet_NumeroProjetAndArchivedFalse(projet.getNumeroProjet());

            int nbTotal = taches.size();
            int nbTerminees = (int) taches.stream()
                    .filter(t -> t.getEtat() == E_EtatTache.TERMINE)
                    .count();

            double progression = nbTotal == 0 ? 0 : (nbTerminees * 100.0 / nbTotal);

            Map<String, Object> data = new HashMap<>();
            data.put("projet", projet.getTitre());
            data.put("progression", progression);  // ex: 75.0 (%)
            comparaison.add(data);
        }

        return comparaison;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getUpcomingDeadlines() {
        LocalDate now = LocalDate.now();
        LocalDate limit = now.plusDays(7); // 7 jours

        return projetRepository.findAll().stream()
                .filter(p -> p.getDateFin() != null
                          && !p.isArchived()
                          && (p.getDateFin().isAfter(now))
                          && (p.getDateFin().isBefore(limit) || p.getDateFin().isEqual(limit)))
                .map(p -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("titre", p.getTitre());
                    data.put("joursRestants", ChronoUnit.DAYS.between(now, p.getDateFin()));
                    data.put("dateFin", p.getDateFin());
                    return data;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Map<String, Map<String, Long>> getProjectsByChefAndStatus() {
        List<Projet> projets = projetRepository.findAll();

        return projets.stream()
                .filter(p -> p.getChefProjet() != null && p.getStatus() != null)
                .collect(Collectors.groupingBy(
                        p -> p.getChefProjet().getNom() + " " + p.getChefProjet().getPrenom(),
                        Collectors.groupingBy(
                                p -> p.getStatus().name(),
                                Collectors.counting()
                        )
                ));
    }

    
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getProjectsForGantt() {
        List<Projet> projets = projetRepository.findAll();

        List<Map<String, Object>> ganttData = new ArrayList<>();

        for (Projet p : projets) {
            if (p.isArchived()) continue; // Ne pas inclure les projets archivés

            // Récupérer les tâches non archivées de ce projet
            List<Tache> taches = tacheRepository.findByProjet_NumeroProjetAndArchivedFalse(p.getNumeroProjet());
            int total = taches.size();
            int done = (int) taches.stream()
                    .filter(t -> t.getEtat() == E_EtatTache.TERMINE)
                    .count();
            double progression = total == 0 ? 0 : (done * 100.0 / total);

            Map<String, Object> projectMap = new HashMap<>();
            projectMap.put("titre", p.getTitre());
            projectMap.put("dateDebut", p.getDateDebut());
            projectMap.put("dateFin", p.getDateFin());
            projectMap.put("progression", progression);

            ganttData.add(projectMap);
        }

        return ganttData;
    }


    @Transactional(readOnly = true)
    public List<ProjetDTO> getProjetsForMember(String matricule) {
        User user = userRepository.findByMatricule(matricule)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable : " + matricule));

        if (user.getRole() != ERole.MEMBER) {
            return Collections.emptyList(); // ou throw une exception si nécessaire
        }

        return projetRepository.findAll().stream()
                .filter(p -> p.getMembres().contains(user) && !p.isArchived())
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Long getNumeroProjetById(Long id) {
        Projet projet = projetRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Projet introuvable"));
        return projet.getNumeroProjet();
    }


 // ProjetService.java
    @Transactional(readOnly = true)
    public Page<ProjetDTO> getProjetsPageForChef(String matriculeChef, Pageable pageable) {
        return projetRepository
            .findByArchivedFalseAndChefProjet_Matricule(matriculeChef, pageable)
            .map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public Page<ProjetDTO> getArchivedProjetsPageForChef(String matriculeChef, Pageable pageable) {
        return projetRepository
            .findByArchivedTrueAndChefProjet_Matricule(matriculeChef, pageable)
            .map(this::mapToDTO);
    }
 // ProjetService.java

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getComparaisonProjetsForChef(String matriculeChef) {
        List<Projet> projets = projetRepository.findByChefProjet_Matricule(matriculeChef);
        List<Map<String, Object>> comparaison = new ArrayList<>();

        for (Projet p : projets) {
            // Tâches non archivées seulement
            List<Tache> taches = tacheRepository.findByProjet_NumeroProjetAndArchivedFalse(p.getNumeroProjet());
            int nbTotal = taches.size();
            int nbTerminees = (int) taches.stream().filter(t -> t.getEtat() == E_EtatTache.TERMINE).count();
            double progression = nbTotal == 0 ? 0 : (nbTerminees * 100.0 / nbTotal);

            Map<String, Object> data = new HashMap<>();
            data.put("projet", p.getTitre());
            data.put("progression", progression);
            comparaison.add(data);
        }
        return comparaison;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getUpcomingDeadlinesForChef(String matriculeChef, int days) {
        LocalDate now = LocalDate.now();
        LocalDate limit = now.plusDays(days);

        return projetRepository.findByChefProjet_MatriculeAndArchivedFalse(matriculeChef).stream()
                .filter(p -> p.getDateFin() != null
                          && p.getDateFin().isAfter(now)
                          && (p.getDateFin().isBefore(limit) || p.getDateFin().isEqual(limit)))
                .map(p -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("titre", p.getTitre());
                    row.put("dateFin", p.getDateFin());
                    row.put("joursRestants", ChronoUnit.DAYS.between(now, p.getDateFin()));
                    return row;
                })
                .sorted(Comparator.comparing(m -> (LocalDate) m.get("dateFin")))
                .collect(Collectors.toList());
    }

 // ProjetService.java

    private Path resolveDiskPathFromDbUrl(String dbValue) {
        // En DB tu stockes "/uploads/files/uuid_nom.ext"
        // On le mappe sur ${uploadDir}/uuid_nom.ext
        // uploadDir vient de @Value et pointe vers "src/main/resources/static/uploads/files"
        String fileName = dbValue;
        int idx = dbValue.lastIndexOf('/');
        if (idx >= 0) fileName = dbValue.substring(idx + 1);
        return Paths.get(uploadDir).resolve(fileName).normalize();
    }

    @Transactional
	public void removeFilesFromProject(Projet projet, List<String> removed) {
        if (removed == null || removed.isEmpty()) return;

        // On bosse sur des valeurs EXACTEMENT identiques à celles stockées en DB (les URLs "/uploads/files/...")
        Set<String> toRemove = new HashSet<>(removed);

        // 1) retirer de la collection persistée
        List<String> restants = projet.getFichiers().stream()
                .filter(path -> !toRemove.contains(path))
                .collect(Collectors.toList());

        // 2) supprimer physiquement chaque fichier ciblé
        for (String pathDb : projet.getFichiers()) {
            if (toRemove.contains(pathDb)) {
                try {
                    Path disk = resolveDiskPathFromDbUrl(pathDb);
                    Files.deleteIfExists(disk);
                } catch (IOException ex) {
                    // Logguer mais ne pas planter toute la transaction pour un fichier manquant
                    // (à toi de décider : throw si tu veux être strict)
                    System.err.println("Suppression fichier échouée: " + pathDb + " -> " + ex.getMessage());
                }
            }
        }

        // 3) appliquer la nouvelle liste
        projet.getFichiers().clear();
        projet.getFichiers().addAll(restants);
    }

   


}