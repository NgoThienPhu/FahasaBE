package com.example.demo.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.account.entity.AdminAccount;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface AdminAccountRepository extends JpaRepository<AdminAccount, String>  {
	
	@Query("""
			SELECT u FROM AdminAccount u
			WHERE u.username = :username
			""")
	Optional<AdminAccount> findByUsername(@Param("username") String username);
	
	@Query("""
			SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END
			FROM AdminAccount u
			""")
	boolean existsAdminAccount();

}
