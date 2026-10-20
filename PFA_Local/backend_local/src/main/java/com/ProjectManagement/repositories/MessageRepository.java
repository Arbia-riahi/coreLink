package com.ProjectManagement.repositories;

import com.ProjectManagement.entities.Message;
import com.ProjectManagement.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
	long countBySeenByNotContainingAndSenderNot(User user, User sender);


}
