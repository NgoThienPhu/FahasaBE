package com.example.demo.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.entities.common.Account;

public class AccountSpecification {

	public static Specification<Account> usernameEquals(String username) {
		return (root, query, cb) -> {
			return cb.equal(root.get("username"), username);
		};
	}
	
}
