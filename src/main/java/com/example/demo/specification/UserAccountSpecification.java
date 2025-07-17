package com.example.demo.specification;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.entities.UserAccount;

public class UserAccountSpecification {

	public static Specification<UserAccount> hasUsername(String username) {
		return (root, query, cb) -> cb.like(root.get("username"), "%" + username + "%");
	}
	
	public static Specification<UserAccount> equalsUsername(String username) {
		return (root, query, cb) -> cb.equal(root.get("username"), username);
	}
	
	public static Specification<UserAccount> hasEmail(String email) {
		return (root, query, cb) -> cb.like(root.get("email"), "%" + email + "%");
	}
	
	public static Specification<UserAccount> equalsEmail(String email) {
		return (root, query, cb) -> cb.equal(root.get("email"), email);
	}
	
	public static Specification<UserAccount> hasPhoneNumber(String phoneNumber) {
		return (root, query, cb) -> cb.like(root.get("phoneNumber"), "%" + phoneNumber + "%");
	}
	
	public static Specification<UserAccount> equalsPhoneNumber(String phoneNumber) {
		return (root, query, cb) -> cb.equal(root.get("phoneNumber"), phoneNumber);
	}
	
	public static Specification<UserAccount> hasFullname(String fullName) {
		return (root, query, cb) -> cb.like(root.get("fullName"), "%" + fullName + "%");
	}
	
}
