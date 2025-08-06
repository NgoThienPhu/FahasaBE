package com.example.demo.services.interfaces;

import java.util.Optional;

import com.example.demo.entities.common.Account;

public interface AccountService {

	Optional<Account> findByUsername(String username);
	
	Boolean existsByUsername(String username);
	
}
