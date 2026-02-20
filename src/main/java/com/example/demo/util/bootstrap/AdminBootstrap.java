package com.example.demo.util.bootstrap;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.account.entity.AdminAccount;
import com.example.demo.account.repository.AdminAccountRepository;
import com.example.demo.email.entity.Email;

@Component
public class AdminBootstrap implements ApplicationRunner {

	private AdminAccountRepository adminAccountRepository;
	private PasswordEncoder passwordEncoder;

	public AdminBootstrap(AdminAccountRepository adminAccountRepository, PasswordEncoder passwordEncoder) {
		this.adminAccountRepository = adminAccountRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void run(ApplicationArguments args) {
		if (adminAccountRepository.existsAdminAccount()) return;

		AdminAccount admin = new AdminAccount();
		admin.setUsername("admin");
		admin.setEmail(new Email("admin@system.com", true));
		admin.setPassword(passwordEncoder.encode("Admin@123"));
		
		adminAccountRepository.save(admin);
	}
}
