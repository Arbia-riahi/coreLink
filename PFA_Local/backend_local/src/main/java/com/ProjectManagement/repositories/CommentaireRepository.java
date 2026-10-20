package com.ProjectManagement.repositories;

import com.ProjectManagement.entities.Commentaire;
import com.ProjectManagement.entities.Tache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentaireRepository extends JpaRepository<Commentaire, Long> {

    List<Commentaire> findByTacheOrderByDateAsc(Tache tache);
}
