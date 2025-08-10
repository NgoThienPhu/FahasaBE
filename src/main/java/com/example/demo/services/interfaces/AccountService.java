package com.example.demo.services.interfaces;

import com.example.demo.entities.common.Account;

public interface AccountService {

	Account findByUsername(String username);
	
	Boolean existsByUsername(String username);
	
}
