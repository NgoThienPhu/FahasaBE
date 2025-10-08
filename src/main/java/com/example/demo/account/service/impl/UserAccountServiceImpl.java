package com.example.demo.account.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.account.dto.AdminChangeUserInfoRequestDTO;
import com.example.demo.account.dto.AdminCreateUserRequestDTO;
import com.example.demo.account.dto.ChangeUserInfoRequestDTO;
import com.example.demo.account.entity.UserAccount;
import com.example.demo.account.repository.AccountRepository;
import com.example.demo.account.repository.UserAccountRepository;
import com.example.demo.account.service.UserAccountService;
import com.example.demo.account.specification.UserAccountSpecification;
import com.example.demo.auth.service.AuthenticationService;
import com.example.demo.email.entity.Email;
import com.example.demo.util.base.entity.PhoneNumber;
import com.example.demo.util.service.RedisService;

import jakarta.transaction.Transactional;

@Service
public class UserAccountServiceImpl extends AccountServiceImpl implements UserAccountService {

	private UserAccountRepository userAccountRepository;

	public UserAccountServiceImpl(AccountRepository accountRepository, PasswordEncoder passwordEncoder,
			RedisService redisService, UserAccountRepository userAccountRepository) {
		super(accountRepository, passwordEncoder, redisService);
		this.userAccountRepository = userAccountRepository;
	}

	@Transactional
	@Override
	public UserAccount adminCreateUserAccount(AdminCreateUserRequestDTO dto) {
		Boolean checkExistsUsername = existsByUsername(dto.username());
		Boolean checkExistsEmail = existsByEmail(dto.email());
		Boolean checkExistsPhoneNumber = existsByPhoneNumber(dto.phoneNumber());

		if (checkExistsUsername)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Tên đăng nhập đã tồn tại, vui lòng thử tên đăng nhập khác");
		if (checkExistsEmail)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email đã tồn tại, vui lòng thử Email khác");
		if (checkExistsPhoneNumber)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Số điện thoại đã tồn tại, vui lòng thử số điện thoại khác");

		return userAccountRepository.save(convertAdminCreateUserRequestDTOToUserAccount(dto));
	}

	@Transactional
	@Override
	public UserAccount changeUserAccountInfo(ChangeUserInfoRequestDTO dto, String userAccountId) {

		UserAccount userAccount = userAccountRepository.findById(userAccountId).orElse(null);

		if (userAccount == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại");
		if (dto.fullName() != null)
			userAccount.setFullName(dto.fullName());
		if (dto.dateOfBirth() != null)
			userAccount.setDateOfBirth(dto.dateOfBirth());
		if (dto.gender() != null)
			userAccount.setGender(UserAccount.Gender.valueOf(dto.gender()));

		return userAccountRepository.save(userAccount);
	}

	@Transactional
	@Override
	public UserAccount adminChangeUserAccountInfo(AdminChangeUserInfoRequestDTO dto, String userAccountId) {

		Boolean checkExistsEmail = existsByEmail(dto.email());
		Boolean checkExistsPhoneNumber = existsByPhoneNumber(dto.phoneNumber());

		if (checkExistsEmail)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email đã tồn tại, vui lòng thử Email khác");
		if (checkExistsPhoneNumber)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Số điện thoại đã tồn tại, vui lòng thử số điện thoại khác");

		UserAccount userAccount = userAccountRepository.findById(userAccountId).orElse(null);

		if (userAccount == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại");

		if (dto.fullName() != null)
			userAccount.setFullName(dto.fullName());
		if (dto.dateOfBirth() != null)
			userAccount.setDateOfBirth(dto.dateOfBirth());
		if (dto.gender() != null)
			userAccount.setGender(UserAccount.Gender.valueOf(dto.gender()));
		if (dto.email() != null)
			userAccount.setEmail(new Email(dto.email()));
		if (dto.phoneNumber() != null)
			userAccount.setPhoneNumber(new PhoneNumber(dto.phoneNumber()));

		return userAccountRepository.save(userAccount);
	}

	@Override
	public Boolean existsByPhoneNumber(String phoneNumber) {
		Specification<UserAccount> spec = UserAccountSpecification.equalsPhoneNumber(phoneNumber);
		return userAccountRepository.count(spec) > 0;
	}

	@Override
	public Page<UserAccount> findUserAccounts(String orderBy, String sortBy, int page, int size) {
		List<String> allowedFields = List.of("username", "email", "phoneNumber");
		if (!allowedFields.contains(sortBy)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Thuộc tính cần sắp xếp không hợp lệ vui lòng thử lại");
		}

		Sort sort = orderBy.equalsIgnoreCase("asc") ? Sort.by(Sort.Direction.ASC, sortBy)
				: Sort.by(Sort.Direction.DESC, sortBy);

		Pageable pageable = PageRequest.of(page, size, sort);

		return userAccountRepository.findAll(pageable);
	}

	@Override
	public Page<UserAccount> findUserAccountByUsernameOrEmailOrPhoneNumber(String keyWord, String orderBy,
			String sortBy, int page, int size) {
		List<String> allowedFields = List.of("username", "email", "phoneNumber");
		if (!allowedFields.contains(sortBy)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Thuộc tính cần sắp xếp không hợp lệ vui lòng thử lại");
		}

		Sort sort = orderBy.equalsIgnoreCase("asc") ? Sort.by(Sort.Direction.ASC, sortBy)
				: Sort.by(Sort.Direction.DESC, sortBy);

		Pageable pageable = PageRequest.of(page, size, sort);

		Specification<UserAccount> spec = Specification.where(UserAccountSpecification.equalsUsername(keyWord))
				.or(UserAccountSpecification.hasEmail(keyWord)).or(UserAccountSpecification.hasPhoneNumber(keyWord));

		return userAccountRepository.findAll(spec, pageable);
	}

	@Transactional
	@Override
	public UserAccount lockUserAccount(String userAccountId) {
		UserAccount user = userAccountRepository.findById(userAccountId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại"));
		if (user.getIsActived() == false)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tài khoản này đã bị khóa, vui lòng thử lại sau");
		user.disabled();
		return userAccountRepository.save(user);
	}

	@Transactional
	@Override
	public UserAccount unlockUserAccount(String userAccountId) {
		UserAccount user = userAccountRepository.findById(userAccountId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại"));
		if (user.getIsActived() == true)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Tài khoản này đã được mở khóa, vui lòng thử lại sau");
		user.active();
		return userAccountRepository.save(user);
	}

	private UserAccount convertAdminCreateUserRequestDTOToUserAccount(AdminCreateUserRequestDTO dto) {
		UserAccount userAccount = new UserAccount();
		userAccount.setUsername(dto.username());
		userAccount.setFullName(dto.fullName());
		userAccount.setEmail(new Email(dto.email()));
		userAccount.setPhoneNumber(new PhoneNumber(dto.phoneNumber()));

		String password = AuthenticationService.generate6DigitCode();
		userAccount.setPassword(passwordEncoder.encode(password));

		userAccount = userAccountRepository.save(userAccount);
		System.out.println(String.format("Mật khẩu của bạn là: %s", password));

		return userAccount;
	}

}
