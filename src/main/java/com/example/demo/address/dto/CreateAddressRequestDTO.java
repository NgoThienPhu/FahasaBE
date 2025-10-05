package com.example.demo.address.dto;

import com.example.demo.account.entity.UserAccount;
import com.example.demo.address.entity.Address;

public record CreateAddressRequestDTO(
		
		String fullName,

		String phoneNumber,

		String addressDetail,

		String city,

		String district,

		String ward,

		Boolean isDefault

) {
	
	public Address convertToEntity(CreateAddressRequestDTO dto, UserAccount userAccount) {
		return new Address(
				dto.fullName, 
				dto.phoneNumber, 
				dto.addressDetail, 
				dto.city, 
				dto.district, 
				dto.ward, 
				dto.isDefault, 
				userAccount
		);
	}
}
