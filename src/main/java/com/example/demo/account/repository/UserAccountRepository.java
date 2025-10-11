package com.example.demo.account.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.account.entity.UserAccount;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
	
	@Query("""
			SELECT u FROM UserAccount u
			WHERE u.username = :username
			""")
	Optional<UserAccount> findByUsername(@Param("username") String username);
	
	@Query("""
		    SELECT u FROM UserAccount u
		    WHERE u.username LIKE CONCAT('%', :keyword, '%')
		    OR u.email.email LIKE CONCAT('%', :keyword, '%')
		    OR u.phoneNumber.phoneNumber LIKE CONCAT('%', :keyword, '%')
			""")
	Page<UserAccount> findByUsernameOrEmailOrPhoneNumber(@Param("keyword") String keyword, Pageable pageable);
	
	@Query("""
			SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END
			FROM UserAccount u
			WHERE u.phoneNumber.phoneNumber = :phoneNumber
			""")
	boolean existsByPhoneNumber(@Param("phoneNumber") String phoneNumber);

}
