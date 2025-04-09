package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.AdminAccount;
import com.example.demo.entities.UserAccount;

@Repository
public interface AdminAccountRepository extends JpaRepository<AdminAccount, String> {

	Optional<UserAccount> findByUsername(String username);
	
}
