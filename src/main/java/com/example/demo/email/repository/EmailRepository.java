package com.example.demo.email.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.demo.email.entity.Email;

@Repository
public interface EmailRepository extends JpaRepository<Email, String>, JpaSpecificationExecutor<Email> {
	
	Optional<Email> findByEmail(String email);
	
	boolean existsByEmail(String email);

}
