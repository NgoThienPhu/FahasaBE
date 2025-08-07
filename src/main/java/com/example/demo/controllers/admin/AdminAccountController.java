package com.example.demo.controllers.admin;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.AdminChangeUserInfoRequestDTO;
import com.example.demo.dto.AdminCreateUserRequestDTO;
import com.example.demo.dto.common.ApiResponseDTO;
import com.example.demo.dto.common.PagedResponseDTO;
import com.example.demo.entities.account.UserAccount;
import com.example.demo.entities.common.Account;
import com.example.demo.services.interfaces.UserAccountService;
import com.example.demo.utils.validation.BindingResultUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/accounts")
public class AdminAccountController {

	private UserAccountService userAccountService;
	
	public AdminAccountController(UserAccountService userAccountService) {
		this.userAccountService = userAccountService;
	}

	@GetMapping
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getUserAccounts(@RequestParam(required = true, defaultValue = "asc") String orderBy,
			@RequestParam(required = true, defaultValue = "username") String sortBy,
			@RequestParam(required = true, defaultValue = "0") int page,
			@RequestParam(required = true, defaultValue = "20") int size) {
		Page<UserAccount> accounts = userAccountService.findUserAccounts(orderBy, sortBy, page, size);
		PagedResponseDTO<UserAccount> pagedResponseDTO = PagedResponseDTO.convertPageToPagedResponseDTO(accounts);
		ApiResponseDTO<PagedResponseDTO<UserAccount>> response = new ApiResponseDTO<PagedResponseDTO<UserAccount>>(
				"Lấy danh sách tài khoản thành công", "success", pagedResponseDTO);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/{id}")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getUserAccountById(@PathVariable("id") String userAccountId) {
		UserAccount account = userAccountService.findUserAccountById(userAccountId);
		if (account == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					String.format("Không tìm thấy tài khoản với id = %s", userAccountId));
		ApiResponseDTO<Account> response = new ApiResponseDTO<Account>("Lấy thông tin tài khoản thành công", "success",
				account);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createAccount(@Valid @RequestBody AdminCreateUserRequestDTO dto, BindingResult result) {
		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result, "Đăng kí thất bại!");
		if (responseError != null)
			return responseError;
		UserAccount account = userAccountService.adminCreateUserAccount(dto);
		ApiResponseDTO<Account> response = new ApiResponseDTO<Account>("Đăng kí thành công", "success", account);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PatchMapping("/{id}")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateProfileUser(@PathVariable("id") String userAccountId, @Valid @RequestBody AdminChangeUserInfoRequestDTO dto, BindingResult result) {
		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result, "Cập nhật thông thất bại");
		if (responseError != null)
			return responseError;
		UserAccount account = userAccountService.adminChangeUserInfo(dto, userAccountId);
		ApiResponseDTO<Account> response = new ApiResponseDTO<Account>("Cập nhật thông tin thành công", "success", account);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PatchMapping("/{id}/lock")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> lockUserAccount(@PathVariable("id") String userAccountId) {
		UserAccount account = userAccountService.lockUserAccount(userAccountId);
		ApiResponseDTO<Account> response = new ApiResponseDTO<Account>("Đã khóa tài khoản thành công", "success",
				account);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PatchMapping("/{id}/unlock")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> unlockUserAccount(@PathVariable("id") String userAccountId) {
		UserAccount account = userAccountService.unlockUserAccount(userAccountId);
		ApiResponseDTO<Account> response = new ApiResponseDTO<Account>("Đã mở khóa tài khoản thành công", "success",
				account);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/{id}/reset-password")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> resetPassword(@PathVariable("id") String userAccountId) {
		userAccountService.resetPassword(userAccountId);
		ApiResponseDTO<Account> response = new ApiResponseDTO<Account>("Làm mới mật khẩu của tài khoản thành công", "success");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
