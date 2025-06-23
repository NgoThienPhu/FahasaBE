package com.example.demo.services.implement;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entities.AdminAccount;
import com.example.demo.entities.bases.Account;
import com.example.demo.entities.enums.AccountType;
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
		
		if(account == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Tài khoản không tồn tại vui lòng thử lại sau");
		
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + AccountType.USER.name());	
		
		if(account instanceof AdminAccount) authority = new SimpleGrantedAuthority("ROLE_" + AccountType.ADMIN.name());
		
		UserDetails userDetails = User.withUsername(account.getUsername()).password(account.getPassword())
				.authorities(authority).build();
		
		return userDetails;
	}

}
