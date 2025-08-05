package com.example.demo.services.implement;

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

import com.example.demo.dto.AdminChangeUserInfoRequestDTO;
import com.example.demo.dto.AdminCreateUserRequestDTO;
import com.example.demo.dto.ChangeUserInfoRequestDTO;
import com.example.demo.entities.Email;
import com.example.demo.entities.PhoneNumber;
import com.example.demo.entities.UserAccount;
import com.example.demo.entities.enums.Gender;
import com.example.demo.repository.UserAccountRepository;
import com.example.demo.services.interfaces.AuthenticationService;
import com.example.demo.services.interfaces.UserAccountService;
import com.example.demo.specification.UserAccountSpecification;

import jakarta.transaction.Transactional;

@Service
public class UserAccountServiceImpl implements UserAccountService {

	private UserAccountRepository userAccountRepository;
	
	private PasswordEncoder passwordEncoder;

	public UserAccountServiceImpl(UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder) {
		this.userAccountRepository = userAccountRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserAccount findUserAccountById(String userAccountId) {
		return userAccountRepository.findById(userAccountId).orElse(null);
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

	@Override
	public UserAccount findUserAccountByUsername(String username) {
		Specification<UserAccount> spec = UserAccountSpecification.equalsUsername(username);
		return userAccountRepository.findOne(spec).orElse(null);
	}

	@Transactional
	@Override
	public UserAccount lockUserAccount(String userAccountId) {
		UserAccount user = userAccountRepository.findById(userAccountId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại"));
		if (user.getIsActive() == false)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tài khoản này đã bị khóa, vui lòng thử lại sau");
		user.setIsActive(false);
		return userAccountRepository.save(user);
	}

	@Transactional
	@Override
	public UserAccount unlockUserAccount(String userAccountId) {
		UserAccount user = userAccountRepository.findById(userAccountId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại"));
		if (user.getIsActive() == true)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tài khoản này đã được mở khóa, vui lòng thử lại sau");
		user.setIsActive(true);
		return userAccountRepository.save(user);
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

	@Transactional
	@Override
	public UserAccount createUserAccount(UserAccount userAccount) {
		return userAccountRepository.save(userAccount);
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
	public UserAccount save(UserAccount userAccount) {
		return userAccountRepository.save(userAccount);
	}
	
	@Override
	public Boolean existsByUsername(String username) {
		Specification<UserAccount> spec = UserAccountSpecification.equalsUsername(username);
		return userAccountRepository.count(spec) > 0;
	}

	@Override
	public Boolean existsByEmail(String email) {
		Specification<UserAccount> spec = UserAccountSpecification.equalsEmail(email);
		return userAccountRepository.count(spec) > 0;
	}

	@Override
	public Boolean existsByPhoneNumber(String phoneNumber) {
		Specification<UserAccount> spec = UserAccountSpecification.equalsPhoneNumber(phoneNumber);
		return userAccountRepository.count(spec) > 0;
	}

	@Transactional
	@Override
	public UserAccount changeUserInfo(ChangeUserInfoRequestDTO dto, String username) {

		UserAccount userAccount = userAccountRepository.findOne(UserAccountSpecification.hasUsername(username))
				.orElse(null);

		if (userAccount == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại");
		if (dto.fullName() != null)
			userAccount.setFullName(dto.fullName());
		if (dto.dateOfBirth() != null)
			userAccount.setDateOfBirth(dto.dateOfBirth());
		if (dto.gender() != null)
			userAccount.setGender(Gender.valueOf(dto.gender()));

		return userAccountRepository.save(userAccount);
	}
	
	@Transactional
	@Override
	public UserAccount adminChangeUserInfo(AdminChangeUserInfoRequestDTO dto, String userAccountId) {
		
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
			userAccount.setGender(Gender.valueOf(dto.gender()));
		if(dto.email() != null)
			userAccount.setEmail(new Email(dto.email()));
		if(dto.phoneNumber() != null)
			userAccount.setPhoneNumber(new PhoneNumber(dto.phoneNumber()));
		
		return userAccountRepository.save(userAccount);
	}
	
	@Override
	public void resetPassword(String userAccountId) {
		UserAccount userAccount = userAccountRepository.findById(userAccountId).orElse(null);
		if (userAccount == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại");
		
		String password = AuthenticationService.generate6DigitCode();
		userAccount.setPassword(passwordEncoder.encode(password));
		
		userAccountRepository.save(userAccount);
		System.out.println(String.format("Mật khẩu mới của bạn là: %s", password));
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
