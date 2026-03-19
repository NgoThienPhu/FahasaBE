package com.example.demo.address.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.address.dto.CreateAddressRequestDTO;
import com.example.demo.address.dto.UpdateAddressRequestDTO;
import com.example.demo.address.entity.Address;
import com.example.demo.address.service.AddressService;
import com.example.demo.util.entity.CustomUserDetails;
import com.example.demo.util.response.ResponseFactory;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/accounts/me/addresses")
public class AddressController {

	private AddressService addressService;
	private ResponseFactory responseFactory;

	public AddressController(AddressService addressService, ResponseFactory responseFactory) {
		this.addressService = addressService;
		this.responseFactory = responseFactory;
	}

	@GetMapping("/{addressId}")
	public ResponseEntity<?> getAddress(@AuthenticationPrincipal CustomUserDetails currentUser,
			@PathVariable String addressId) {
		Address address = addressService.findById(addressId, currentUser.getId());
		return responseFactory.success(address, "Lấy chi tiết địa chỉ người dùng thành công");
	}

	@GetMapping
	public ResponseEntity<?> listAddresses(@AuthenticationPrincipal CustomUserDetails currentUser) {
		List<Address> addresses = addressService.findAll(currentUser.getId());
		return responseFactory.success(addresses, "Lấy danh sách địa chỉ người dùng thành công");
	}

	@PostMapping
	public ResponseEntity<?> addAddress(@AuthenticationPrincipal CustomUserDetails currentUser,
			@Valid @RequestBody CreateAddressRequestDTO body) {
		Address address = addressService.createAddress(body, currentUser.getId());
		return responseFactory.success(address, "Thêm địa chỉ giao hàng thành công",
				org.springframework.http.HttpStatus.CREATED);
	}

	@PutMapping("/{addressId}")
	public ResponseEntity<?> updateAddress(@AuthenticationPrincipal CustomUserDetails currentUser,
			@PathVariable String addressId, @Valid @RequestBody UpdateAddressRequestDTO body) {
		Address address = addressService.updateById(body, addressId, currentUser.getId());
		return responseFactory.success(address, "Cập nhật địa chỉ giao hàng thành công");
	}

	@DeleteMapping("/{addressId}")
	public ResponseEntity<?> deleteAddress(@AuthenticationPrincipal CustomUserDetails currentUser,
			@PathVariable String addressId) {
		addressService.deleteById(addressId, currentUser.getId());
		return responseFactory.success("Xóa địa chỉ người dùng thành công");
	}

}