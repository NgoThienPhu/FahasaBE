package com.example.demo.address.application;


import java.util.List;

import com.example.demo.address.dto.CreateAddressRequestDTO;
import com.example.demo.address.entity.Address;

public interface AddressApplicationService {

	Address create(CreateAddressRequestDTO body, String accountId);
	
	Address findById(String addressId, String accountId);
	
	List<Address> findAll(String accountId);
	
	void deleteById(String addressId, String accountId);
	
}
