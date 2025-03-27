package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.bases.Account;
import com.example.demo.entities.enums.AccountRole;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    Optional<Account> findByUsername(String username);

    Optional<Account> findByEmail(String email);

    List<Account> findByRole(AccountRole role);

    @Query("SELECT a FROM Account a WHERE a.username LIKE %:keyword% OR a.email LIKE %:keyword%")
    List<Account> searchByKeyword(@Param("keyword") String keyword);
    
}
