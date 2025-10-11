package com.example.demo.address.specification;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.address.entity.Address;

public class AddressSpecification {
	
	public static Specification<Address> hasUserAccountId(String username) {
		return (root, query, cb) -> cb.equal(root.get("userAccount").get("id"), username);
	}
	
	public static Specification<Address> isDefault(Boolean isDefault) {
		return (root, query, cb) -> cb.equal(root.get("isDefault"), isDefault);
	}
	
}
