package com.example.demo.address.service;

import java.util.List;

import com.example.demo.address.entity.Address;

public interface AddressService {
	
	List<Address> findAllByUsername(String username);
	
	Address findByIdAndUsername(String addressId, String username);
	
	void deleteByIdAndUsername(String addressId, String username);
	
}
