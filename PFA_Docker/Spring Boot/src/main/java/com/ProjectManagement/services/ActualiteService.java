package com.ProjectManagement.services;

import com.ProjectManagement.entities.Actualite;
import com.ProjectManagement.repositories.ActualiteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ActualiteService {

    // Chemin où les images seront stockées dans Docker (volume monté dans docker-compose)
    private final Path root = Paths.get("/app/uploads/files");

    @Autowired
    private ActualiteRepository actualiteRepository;

    public List<Actualite> findAll() {
        List<Actualite> actualites = actualiteRepository.findAll();
        actualites.forEach(this::buildImageUrls);
        return actualites;
    }

    public Actualite findById(Long id) {
        return actualiteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Actualité introuvable : " + id));
    }

    @Transactional
    public Actualite create(List<MultipartFile> files, String titre, String description) throws IOException {
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("Vous devez uploader au moins une image !");
        }

        List<String> imageNames = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.getContentType().startsWith("image/")) {
                throw new IllegalArgumentException("Seules les images sont autorisées !");
            }

            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }

            String newImageName = getSaltString() + "_" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), this.root.resolve(newImageName));
            imageNames.add(newImageName);
        }

        Actualite actualite = new Actualite(titre, description, imageNames);
        return buildImageUrls(actualiteRepository.save(actualite));
    }

    @Transactional
    public Actualite update(Long id, String titre, String description, List<MultipartFile> files) throws IOException {
        return actualiteRepository.findById(id).map(actualite -> {

            actualite.setTitre(titre);
            actualite.setDescription(description);

            if (files != null && !files.isEmpty()) {
                List<String> imageNames = new ArrayList<>();
                for (MultipartFile file : files) {
                    if (!file.getContentType().startsWith("image/")) {
                        throw new RuntimeException("Seules les images sont autorisées !");
                    }

                    String newImageName = getSaltString() + "_" + file.getOriginalFilename();
                    try {
                        Files.copy(file.getInputStream(), this.root.resolve(newImageName));
                        imageNames.add(newImageName);
                    } catch (IOException e) {
                        throw new RuntimeException("Impossible de sauvegarder le fichier : " + e.getMessage());
                    }
                }
                actualite.setPhotos(imageNames);
            }

            return buildImageUrls(actualiteRepository.save(actualite));

        }).orElseThrow(() -> new EntityNotFoundException("Actualité introuvable : " + id));
    }

    @Transactional
    public Actualite delete(Long actualiteId) {
        return actualiteRepository.findById(actualiteId).map(actualite -> {
            actualiteRepository.delete(actualite);
            return actualite;
        }).orElseThrow(() -> new EntityNotFoundException("Actualité introuvable : " + actualiteId));
    }

    /** Générer un nom unique pour les images */
    protected static String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }

    /** Construire l’URL publique dynamique */
   public Actualite buildImageUrls(Actualite actualite) {
    if (actualite.getPhotos() != null) {
        List<String> fullUrls = new ArrayList<>();
        for (String photo : actualite.getPhotos()) {
            fullUrls.add("/uploads/files/" + photo);
        }
        actualite.setPhotos(fullUrls);
    }
    return actualite;
}

}
