package com.example.demo.services.interfaces;

import java.util.Optional;

import com.example.demo.entities.AdminAccount;

public interface AdminAccountService {
	
	Optional<AdminAccount> findByUsername(String username);
	
}
