package com.example.demo.services.implement;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entities.account.AdminAccount;
import com.example.demo.entities.common.Account;
import com.example.demo.services.interfaces.AccountService;
import com.example.demo.services.interfaces.CustomUserDetailService;

@Service
public class CustomUserDetailServiceImpl implements CustomUserDetailService {
	
	private AccountService accountService;
	
	public CustomUserDetailServiceImpl(AccountService accountService) {
		this.accountService = accountService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Account account = accountService.findByUsername(username);
		
		if(account == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Tài khoản không tồn tại vui lòng thử lại sau");
		
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + Account.AccountType.USER.name());	
		
		if(account instanceof AdminAccount) authority = new SimpleGrantedAuthority("ROLE_" + Account.AccountType.ADMIN.name());
		
		UserDetails userDetails = User.withUsername(account.getUsername()).password(account.getPassword())
				.authorities(authority).build();
		
		return userDetails;
	}

}
