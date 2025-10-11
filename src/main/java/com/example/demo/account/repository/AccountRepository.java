package com.example.demo.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.account.entity.base.Account;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
	
	@Query("""
			SELECT u FROM Account u
			WHERE u.username = :username
			""")
	Optional<Account> findByUsername(@Param("username") String username);
	
	@Query("""
			SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END
			FROM Account u
			WHERE u.username = :username
			""")
	boolean existsByUsername(@Param("username") String username);
	
	@Query("""
			SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END
			FROM Account u
			WHERE u.email.email = :email
			""")
	boolean existsByEmail(@Param("email") String email);
    
}
