package com.example.demo.address.service.impl;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.address.entity.Address;
import com.example.demo.address.repository.AddressRepository;
import com.example.demo.address.service.AddressService;
import com.example.demo.address.specification.AddressSpecification;

@Service
public class AddressServiceImpl implements AddressService {

	private AddressRepository addressRepository;

	public AddressServiceImpl(AddressRepository addressRepository) {
		this.addressRepository = addressRepository;
	}

	@Override
	public List<Address> findAllByUsername(String username) {
		return addressRepository.findAll(AddressSpecification.hasUsername(username));
	}

	@Override
	public Address findByIdAndUsername(String addressId, String username) {
		Specification<Address> spec = Specification.where(AddressSpecification.hasId(addressId))
				.and(AddressSpecification.hasUsername(username));

		return addressRepository.findOne(spec).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
				"Địa chỉ cần tìm không tồn tại, vui lòng thử lại sau"));
	}

	@Override
	public void deleteByIdAndUsername(String addressId, String username) {
		Specification<Address> spec = Specification.where(AddressSpecification.hasId(addressId))
				.and(AddressSpecification.hasUsername(username));

		addressRepository.delete(spec);
	}

}
