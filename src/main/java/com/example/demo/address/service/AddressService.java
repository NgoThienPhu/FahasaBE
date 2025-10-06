package com.example.demo.address.service;

import java.util.List;

import com.example.demo.address.entity.Address;

public interface AddressService {
	
	Address save(Address address);
	
	List<Address> findAll(String accountId);
	
	Address findById(String addressId, String accountId);
	
	void deleteById(String addressId, String accountId);
	
	void ressetDefaultAddress(String accountId);
	
}
