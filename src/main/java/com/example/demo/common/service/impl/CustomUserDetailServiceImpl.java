package com.example.demo.common.service.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.account.entity.AdminAccount;
import com.example.demo.account.entity.base.Account;
import com.example.demo.account.service.AccountService;
import com.example.demo.common.base.entity.CustomUserDetails;
import com.example.demo.common.service.CustomUserDetailService;

@Service
public class CustomUserDetailServiceImpl implements CustomUserDetailService {
	
	private AccountService accountService;
	
	public CustomUserDetailServiceImpl(AccountService accountService) {
		this.accountService = accountService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Account account = accountService.findAccountByUsername(username);
		
		if(account == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Tài khoản không tồn tại vui lòng thử lại sau");
		
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + Account.AccountType.USER.name());	
		
		if(account instanceof AdminAccount) authority = new SimpleGrantedAuthority("ROLE_" + Account.AccountType.ADMIN.name());
		
		CustomUserDetails customUserDetails =  new CustomUserDetails(account.getId(), username, account.getPassword(), List.of(authority));
		
		return customUserDetails;
	}

}
