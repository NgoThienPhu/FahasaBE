package com.example.demo.services.implement;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entities.bases.Account;
import com.example.demo.repository.AccountRepository;
import com.example.demo.services.interfaces.CustomUserDetailServiceInf;

@Service
public class CustomUserDetailService implements CustomUserDetailServiceInf {
	
	private AccountRepository accountRepository;

	public CustomUserDetailService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = accountRepository.findByUsername(username).orElse(null);
		if(account == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Tài khoản hoặc mật khẩu không chính xác");
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + account.getRole());
		UserDetails userDetails = User.withUsername(account.getUsername()).password(account.getPassword())
				.authorities(authority).build();
		return userDetails;
	}

}
