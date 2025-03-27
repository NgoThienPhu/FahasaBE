package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.AdminAccount;

@Repository
public interface AdminAccountRepository extends JpaRepository<AdminAccount, String> {

}
