package com.example.demo.address.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.address.dto.CreateAddressRequestDTO;
import com.example.demo.address.dto.UpdateAddressRequestDTO;
import com.example.demo.address.entity.Address;
import com.example.demo.address.service.AddressService;
import com.example.demo.util.dto.api_response.ApiResponseDTO;
import com.example.demo.util.dto.api_response.ApiResponseSuccessDTO;
import com.example.demo.util.entity.CustomUserDetails;
import com.example.demo.util.validation.BindingResultUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/accounts/me/addresses")
public class AddressController {

	private AddressService addressService;

	public AddressController(AddressService addressService) {
		this.addressService = addressService;
	}

	@GetMapping("/{addressId}")
	public ResponseEntity<?> findById(@PathVariable String addressId,
			@AuthenticationPrincipal CustomUserDetails currentUser) {
		Address address = addressService.findById(addressId, currentUser.getId());
		var response = new ApiResponseSuccessDTO<Address>(200, "Lấy chi tiết địa chỉ người dùng thành công", address);
		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<?> findAll(@AuthenticationPrincipal CustomUserDetails currentUser) {
		List<Address> addresses = addressService.findAll(currentUser.getId());
		var response = new ApiResponseSuccessDTO<List<Address>>(200, "Lấy danh sách địa chỉ người dùng thành công",
				addresses);
		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody CreateAddressRequestDTO body, HttpServletRequest request,
			BindingResult result, @AuthenticationPrincipal CustomUserDetails currentUser) {
		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result, "Thêm mới địa chỉ thất bại!",
				request.getRequestURI());
		if (responseError != null)
			return responseError;

		Address address = addressService.createAddress(body, currentUser.getId());
		var response = new ApiResponseSuccessDTO<Address>(201, "Thêm địa chỉ giao hàng thành công", address);
		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.OK);
	}

	@PutMapping("/{addressId}")
	public ResponseEntity<?> update(@PathVariable String addressId, @Valid @RequestBody UpdateAddressRequestDTO body,
			HttpServletRequest request, BindingResult result, @AuthenticationPrincipal CustomUserDetails currentUser) {
		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result, "Cập nhật địa chỉ thất bại!",
				request.getRequestURI());
		if (responseError != null)
			return responseError;

		Address address = addressService.updateById(body, addressId, currentUser.getId());

		var response = new ApiResponseSuccessDTO<Address>(200, "Cập nhật chỉ giao hàng thành công", address);
		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{addressId}")
	public ResponseEntity<?> deleteByIdAndUsername(@PathVariable String addressId,
			@AuthenticationPrincipal CustomUserDetails currentUser) {
		addressService.deleteById(addressId, currentUser.getId());
		var response = new ApiResponseSuccessDTO<Void>(200, "Xóa địa chỉ người dùng thành công");
		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.OK);
	}
}
