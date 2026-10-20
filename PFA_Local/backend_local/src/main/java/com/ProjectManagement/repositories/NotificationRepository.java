package com.ProjectManagement.repositories;

import com.ProjectManagement.entities.*;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByDestinataire_MatriculeOrderByDateDesc(String matricule);

    List<Notification> findByDestinataire_MatriculeAndLueFalseOrderByDateDesc(String matricule);

    @Transactional
    @Modifying
    @Query("UPDATE Notification n SET n.lue = true WHERE n.destinataire.matricule = :matricule")
    void markAllAsRead(String matricule);
    
    long countByDestinataireMatriculeAndLueFalse(String matricule);

}


