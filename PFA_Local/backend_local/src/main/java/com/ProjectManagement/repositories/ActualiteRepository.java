package com.ProjectManagement.repositories;

import com.ProjectManagement.entities.Actualite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActualiteRepository extends JpaRepository<Actualite, Long> {

    // Déjà présent
    List<Actualite> findAllByOrderByDateCreationDesc();

    // Pour la pagination + tri
    Page<Actualite> findAllByOrderByDateCreationDesc(Pageable pageable);
}
