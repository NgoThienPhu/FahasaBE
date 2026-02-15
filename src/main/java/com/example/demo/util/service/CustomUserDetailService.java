package com.example.demo.util.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.account.entity.AdminAccount;
import com.example.demo.account.entity.base.Account;
import com.example.demo.account.repository.AccountRepository;
import com.example.demo.util.entity.CustomUserDetails;
import com.example.demo.util.exception.CustomException;
import com.example.demo.util.service.CustomUserDetailService;

@Service
public class CustomUserDetailService implements UserDetailsService {

	private AccountRepository accountRepository;

	public CustomUserDetailService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Account account = accountRepository.findByUsername(username)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại"));

		SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + Account.AccountType.USER.name());

		if (account instanceof AdminAccount)
			authority = new SimpleGrantedAuthority("ROLE_" + Account.AccountType.ADMIN.name());

		CustomUserDetails customUserDetails = new CustomUserDetails(account.getId(), username, account.getPassword(),
				List.of(authority));

		return customUserDetails;
	}

}
