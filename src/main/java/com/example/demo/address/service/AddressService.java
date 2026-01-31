package com.example.demo.address.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.address.dto.UpdateAddressRequestDTO;
import com.example.demo.address.entity.Address;
import com.example.demo.address.repository.AddressRepository;
import com.example.demo.address.service.AddressService;

@Service
public class AddressService {

	private AddressRepository addressRepository;

	public AddressService(AddressRepository addressRepository) {
		this.addressRepository = addressRepository;
	}

	public List<Address> findAllByUserAccountId(String userAccountId) {
		return addressRepository.findAllByUserAccountId(userAccountId);
	}

	public Address findById(String addressId, String userAccountId) {

		return addressRepository.findByIdAndUserAccountId(addressId, userAccountId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Địa chỉ không tồn tại"));
	}
	
	public Address findDefaultAddress(String userAccountId) {
		return addressRepository.findDefaultAddress(userAccountId).orElse(null);
	}

	public void deleteById(String addressId, String userAccountId) {
		Address address = findById(addressId, userAccountId);
		addressRepository.delete(address);
	}
	
	public Address updateById(UpdateAddressRequestDTO body ,String addressId, String userAccountId) {
		Address address = findById(addressId, userAccountId);
		address.setFullName(body.getFullName());
		address.setPhoneNumber(body.getPhoneNumber());
		address.setAddressDetail(body.getAddressDetail());
		address.setCity(body.getCity());
		address.setDistrict(body.getDistrict());
		address.setWard(body.getWard());
		
		if(!address.getIsDefault()) {
			if(body.getIsDefault()) {
				ressetDefaultAddress(userAccountId);
				address.setIsDefault(body.getIsDefault());
			}
		}
		return save(address);
	}

	public void ressetDefaultAddress(String userAccountId) {
		Address defaultAddress = addressRepository.findDefaultAddress(userAccountId).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy địa chỉ mặc định"));
		defaultAddress.setIsDefault(false);
		addressRepository.save(defaultAddress);
	}

	public Address save(Address address) {
		return addressRepository.save(address);
	}

}
