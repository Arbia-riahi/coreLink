package com.ProjectManagement.repositories;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



import com.ProjectManagement.entities.User;
import com.ProjectManagement.entities.ERole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.*;


@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByMatricule(String matricule);

    boolean existsByMatricule(String matricule);

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);


    // Pagination sans ADMIN
    Page<User> findByRoleNot(ERole role, Pageable pageable);

    List<User> findByRole(ERole role);

    // Recherche par token de reset pour mot de passe oublié
    Optional<User> findByResetToken(String token);
    
    List<User> findByIsDeletedFalse();
    Page<User> findByRoleNotAndIsDeletedFalse(ERole role, Pageable pageable);
    long countByRoleAndIsDeletedFalse(ERole role);

    
   
    public List<User> findByRoleAndIsDeletedFalse(ERole role);
    Optional<User> findByNomAndPrenom(String nom, String prenom);
    Page<User> findByRoleNotAndIsDeletedFalseOrderByDateCreationDesc(ERole role, Pageable pageable);

   
    
    @Query("""
    		  SELECT u FROM User u
    		  WHERE u.isDeleted = false
    		    AND u.role <> :admin
    		    AND (
    		         LOWER(u.matricule) LIKE :q ESCAPE '!'
    		      OR LOWER(u.nom)       LIKE :q ESCAPE '!'
    		      OR LOWER(u.prenom)    LIKE :q ESCAPE '!'
    		      OR LOWER(u.email)     LIKE :q ESCAPE '!'
    		      OR (:roleMatch = true AND u.role = :role)
    		    )
    		""")
    		Page<User> searchNonAdmin(@Param("q") String q,
    		                          @Param("admin") ERole admin,
    		                          @Param("roleMatch") boolean roleMatch,
    		                          @Param("role") ERole role,
    		                          Pageable pageable);

    @Query("""
    		  SELECT u FROM User u
    		  WHERE u.isDeleted = false
    		    AND u.role <> :admin
    		    AND (
    		         LOWER(u.matricule) LIKE :q ESCAPE '!'
    		      OR LOWER(u.nom)       LIKE :q ESCAPE '!'
    		      OR LOWER(u.prenom)    LIKE :q ESCAPE '!'
    		      OR LOWER(u.email)     LIKE :q ESCAPE '!'
    		      OR LOWER(CAST(u.role as string)) LIKE :q ESCAPE '!'
    		    )
    		""")
    		Page<User> searchNonAdmin(@Param("q") String q,
    		                          @Param("admin") ERole admin,
    		                          Pageable pageable);

    
  



}













    
   





  
  
 
