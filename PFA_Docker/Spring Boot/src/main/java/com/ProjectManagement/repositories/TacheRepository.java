package com.ProjectManagement.repositories;

import com.ProjectManagement.entities.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TacheRepository extends JpaRepository<Tache, Long> {

    List<Tache> findByProjetNumeroProjet(Long numeroProjet);
   


    List<Tache> findByEtat(E_EtatTache etat);

    List<Tache> findByProjetNumeroProjetAndEtat(Long numeroProjet, E_EtatTache etat);
    
    List<Tache> findByProjet_NumeroProjet(Long numeroProjet);
    List<Tache> findByProjet_NumeroProjetAndArchivedFalse(Long numeroProjet);
    List<Tache> findByProjetNumeroProjetAndArchivedTrue(Long numeroProjet);




@Query("""
	    SELECT DISTINCT t
	    FROM Tache t
	    LEFT JOIN t.autresAssignes a
	    WHERE t.archived = false
	      AND (t.assigneeA.matricule = :matricule OR a.matricule = :matricule)
	""")
	List<Tache> findActiveByAnyAssignment(@Param("matricule") String matricule);


}



