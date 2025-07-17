package com.example.demo.specification;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.entities.AdminAccount;

public class AdminAccountSpecification {

	public static Specification<AdminAccount> usernameEquals(String username) {
		return (root, query, cb) -> {
			return cb.equal(root.get("username"), username);
		};
	}
	
}
