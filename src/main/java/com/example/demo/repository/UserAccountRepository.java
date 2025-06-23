package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.UserAccount;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {

	Optional<UserAccount> findByUsername(String username);

	Optional<UserAccount> findByEmail(String email);
	
	Boolean existsByEmail(String email);
	
	Boolean existsByPhoneNumber(String phoneNumber);

	List<UserAccount> findByFullNameContaining(String fullName);

	@Query("SELECT a FROM UserAccount a WHERE a.username LIKE %:keyword% OR a.email LIKE %:keyword% OR a.phoneNumber LIKE %:keyword%")
	List<UserAccount> searchByUsernameEmailPhoneNumber(@Param("keyword") String keyword);

}
