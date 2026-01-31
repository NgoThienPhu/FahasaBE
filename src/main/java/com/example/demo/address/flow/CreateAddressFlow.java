package com.example.demo.address.flow;

import org.springframework.stereotype.Service;

import com.example.demo.account.entity.UserAccount;
import com.example.demo.account.service.AccountService;
import com.example.demo.address.dto.CreateAddressRequestDTO;
import com.example.demo.address.entity.Address;
import com.example.demo.address.service.AddressService;

@Service
public class CreateAddressFlow {
	
	private AccountService accountService;
	
	private AddressService addressService;
	
	public CreateAddressFlow(AccountService accountService, AddressService addressService) {
		this.accountService = accountService;
		this.addressService = addressService;
	}

	public Address createAddress(CreateAddressRequestDTO body, String accountId) {
		UserAccount user = (UserAccount) accountService.findById(accountId);
		Address addressDefault = addressService.findDefaultAddress(accountId);
		if(body.getIsDefault() == true && addressDefault != null) addressService.ressetDefaultAddress(accountId);
		else body.setIsDefault(true);
		Address myAddress = body.convertToEntity(body, user);
		return addressService.save(myAddress);
	}

}
