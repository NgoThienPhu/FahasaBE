package com.example.demo.services.implement;

import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.demo.entities.AdminAccount;
import com.example.demo.repository.AdminAccountRepository;
import com.example.demo.services.interfaces.AdminAccountService;
import com.example.demo.specification.AdminAccountSpecification;

@Service
public class AdminAccountServiceImpl implements AdminAccountService {
	
	private AdminAccountRepository adminAccountRepository;

	public AdminAccountServiceImpl(AdminAccountRepository adminAccountRepository) {
		this.adminAccountRepository = adminAccountRepository;
	}

	@Override
	public Optional<AdminAccount> findByUsername(String username) {
		Specification<AdminAccount> spec = AdminAccountSpecification.usernameEquals(username);
		return adminAccountRepository.findOne(spec);
	}

}
