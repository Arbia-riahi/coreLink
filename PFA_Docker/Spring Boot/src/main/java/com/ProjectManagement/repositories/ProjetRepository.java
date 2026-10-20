package com.ProjectManagement.repositories;

import com.ProjectManagement.entities.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ProjetRepository extends JpaRepository<Projet, Long>, JpaSpecificationExecutor<Projet> {

 Page<Projet> findByArchivedFalse(Pageable pageable);
 Page<Projet> findByArchivedTrue(Pageable pageable);
 List<Projet> findByArchivedTrue();

 long countByStatus(EProjet status);

 @Query("""
     select distinct p
     from Projet p
     left join fetch p.membres m
     left join fetch p.chefProjet cp
     where p.archived = false and p.dateFin is not null
 """)
 List<Projet> findAllWithMembresEtChef();

 Page<Projet> findByArchivedFalseAndChefProjet_Matricule(String matricule, Pageable pageable);
 Page<Projet> findByArchivedTrueAndChefProjet_Matricule(String matricule, Pageable pageable);

 List<Projet> findByChefProjet_Matricule(String matricule);
 List<Projet> findByChefProjet_MatriculeAndArchivedFalse(String matricule);
}


