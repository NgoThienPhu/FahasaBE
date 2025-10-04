package com.example.demo.email.specification;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.email.entity.Email;

public class EmailSpecification {

	public static Specification<Email> hasEmail(String email) {
		return (root, query, cb) -> {
			return cb.equal(root.get("email"), email);
		};
	}
	
}
