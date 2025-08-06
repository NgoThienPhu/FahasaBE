package com.example.demo.specification;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.entities.account.Account;

public class AccountSpecification {

	public static Specification<Account> usernameEquals(String username) {
		return (root, query, cb) -> {
			return cb.equal(root.get("username"), username);
		};
	}
	
}
