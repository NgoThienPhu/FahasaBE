package com.example.demo.address.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.address.dto.CreateAddressRequestDTO;
import com.example.demo.address.entity.Address;
import com.example.demo.address.flow.CreateAddressFlow;
import com.example.demo.address.service.AddressService;
import com.example.demo.util.base.dto.ApiResponseDTO;
import com.example.demo.util.base.entity.CustomUserDetails;

@RestController
@RequestMapping("/api/accounts/me/addresses")
public class AddressController {

	private AddressService addressService;
	
	private CreateAddressFlow createAddressFlow;
	
	public AddressController(AddressService addressService, CreateAddressFlow createAddressFlow) {
		this.addressService = addressService;
		this.createAddressFlow = createAddressFlow;
	}

	@GetMapping("/{addressId}")
	public ResponseEntity<?> findById(@PathVariable String addressId,
			@AuthenticationPrincipal CustomUserDetails currentUser) {
		Address address = addressService.findByIdAndUserAccountId(addressId, currentUser.getId());
		var response = new ApiResponseDTO<Address>("Lấy chi tiết địa chỉ người dùng thành công", true, address);
		return new ResponseEntity<ApiResponseDTO<Address>>(response, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<?> findAll(@AuthenticationPrincipal CustomUserDetails currentUser) {
		List<Address> addresses = addressService.findAllByUserAccountId(currentUser.getId());
		var response = new ApiResponseDTO<List<Address>>("Lấy danh sách địa chỉ người dùng thành công", true, addresses);
		return new ResponseEntity<ApiResponseDTO<List<Address>>>(response, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<?> create(@RequestBody CreateAddressRequestDTO body, @AuthenticationPrincipal CustomUserDetails currentUser) {
		Address address = createAddressFlow.createAddress(body, currentUser.getId());
		var response = new ApiResponseDTO<Address>("Thêm địa chỉ giao hàng thành công", true, address);
		return new ResponseEntity<ApiResponseDTO<Address>>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{addressId}")
	public ResponseEntity<?> deleteByIdAndUsername(@PathVariable String addressId,
			@AuthenticationPrincipal CustomUserDetails currentUser) {
		addressService.deleteById(addressId, currentUser.getId());
		var response = new ApiResponseDTO<Void>("Xóa địa chỉ người dùng thành công", true);
		return new ResponseEntity<ApiResponseDTO<Void>>(response, HttpStatus.OK);
	}
}
