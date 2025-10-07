package com.example.demo.address.service.impl;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.account.entity.UserAccount;
import com.example.demo.account.service.AccountReader;
import com.example.demo.address.dto.CreateAddressRequestDTO;
import com.example.demo.address.entity.Address;
import com.example.demo.address.repository.AddressRepository;
import com.example.demo.address.service.AddressService;
import com.example.demo.address.specification.AddressSpecification;

@Service
public class AddressServiceImpl implements AddressService {

	private AddressRepository addressRepository;
	
	private AccountReader accountReader;

	public AddressServiceImpl(AddressRepository addressRepository, AccountReader accountReader) {
		this.addressRepository = addressRepository;
		this.accountReader = accountReader;
	}

	@Override
	public Address create(CreateAddressRequestDTO body, String accountId) {
		UserAccount user = (UserAccount) accountReader.findById(accountId);
		if(body.isDefault() == true) ressetDefaultAddress(accountId);
		Address myAddress = body.convertToEntity(body, user);
		return addressRepository.save(myAddress);
	}

	@Override
	public List<Address> findAll(String accountId) {
		return addressRepository.findAll(AddressSpecification.hasUserAccountId(accountId));
	}

	@Override
	public Address findById(String addressId, String username) {
		Specification<Address> spec = Specification.where(AddressSpecification.hasId(addressId))
				.and(AddressSpecification.hasUserAccountId(username));

		return addressRepository.findOne(spec).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
				"Địa chỉ cần tìm không tồn tại, vui lòng thử lại sau"));
	}

	@Override
	public void deleteById(String addressId, String username) {
		Specification<Address> spec = Specification.where(AddressSpecification.hasId(addressId))
				.and(AddressSpecification.hasUserAccountId(username));
		addressRepository.delete(spec);
	}

	@Override
	public void ressetDefaultAddress(String accountId) {
		Address defaultAddress = addressRepository.findOne(AddressSpecification.isDefault(true)).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy địa chỉ mặc định"));
		defaultAddress.setIsDefault(false);
		addressRepository.save(defaultAddress);
	}

}
