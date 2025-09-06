package com.example.demo.address.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.address.entity.Address;
import com.example.demo.address.service.AddressService;
import com.example.demo.common.base.dto.ApiResponseDTO;

@RestController
@RequestMapping("/api/accounts/me/addresses")
public class AddressController {

	private AddressService addressService;

	public AddressController(AddressService addressService) {
		this.addressService = addressService;
	}

	@GetMapping("/{addressId}")
	public ResponseEntity<?> findByIdAndUsername(@PathVariable String addressId,
			@AuthenticationPrincipal UserDetails currentUser) {
		Address address = addressService.findByIdAndUsername(addressId, currentUser.getUsername());
		var response = new ApiResponseDTO<Address>("Lấy chi tiết địa chỉ người dùng thành công", true, address);
		return new ResponseEntity<ApiResponseDTO<Address>>(response, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<?> findAllByUsername(@AuthenticationPrincipal UserDetails currentUser) {
		List<Address> addresses = addressService.findAllByUsername(currentUser.getUsername());
		var response = new ApiResponseDTO<List<Address>>("Lấy danh sách địa chỉ người dùng thành công", true, addresses);
		return new ResponseEntity<ApiResponseDTO<List<Address>>>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{addressId}")
	public ResponseEntity<?> deleteByIdAndUsername(@PathVariable String addressId,
			@AuthenticationPrincipal UserDetails currentUser) {
		addressService.deleteByIdAndUsername(addressId, addressId);
		var response = new ApiResponseDTO<Void>("Xóa địa chỉ người dùng thành công", true);
		return new ResponseEntity<ApiResponseDTO<Void>>(response, HttpStatus.OK);
	}
}
