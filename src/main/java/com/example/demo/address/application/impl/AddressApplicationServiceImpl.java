package com.example.demo.address.application.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.account.entity.UserAccount;
import com.example.demo.account.service.UserAccountService;
import com.example.demo.address.application.AddressApplicationService;
import com.example.demo.address.dto.CreateAddressRequestDTO;
import com.example.demo.address.entity.Address;
import com.example.demo.address.service.AddressService;

@Service
public class AddressApplicationServiceImpl implements AddressApplicationService {

	private AddressService addressService;

	private UserAccountService userAccountService;

	public AddressApplicationServiceImpl(AddressService addressService, UserAccountService userAccountService) {
		this.addressService = addressService;
		this.userAccountService = userAccountService;
	}

	@Override
	public Address create(CreateAddressRequestDTO body, String accountId) {
		UserAccount user = (UserAccount) userAccountService.findAccountById(accountId);
		Address myAddress = body.convertToEntity(body, user);
		return addressService.save(myAddress);
	}

	@Override
	public Address findById(String addressId, String accountId) {
		return addressService.findById(addressId, accountId);
	}

	@Override
	public List<Address> findAll(String accountId) {
		return addressService.findAll(accountId);
	}

	@Override
	public void deleteById(String addressId, String accountId) {
		addressService.deleteById(addressId, accountId);

	}

}
