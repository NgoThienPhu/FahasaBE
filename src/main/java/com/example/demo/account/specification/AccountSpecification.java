package com.example.demo.account.specification;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.account.entity.base.Account;

public class AccountSpecification {

	public static Specification<Account> hasUsername(String username) {
		return (root, query, cb) -> {
			return cb.equal(root.get("username"), username);
		};
	}
	
	public static Specification<Account> hasEmail(String email) {
		return (root, query, cb) -> {
			return cb.equal(root.get("email"), email);
		};
	}
	
	public static Specification<Account> hasType(Class<? extends Account> type) {
		return (root, query, cb) -> cb.equal(root.type(), type);
	}
	
}
