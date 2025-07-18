package com.example.demo.services.implement;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entities.UserAccount;
import com.example.demo.repository.UserAccountRepository;
import com.example.demo.services.interfaces.UserAccountService;
import com.example.demo.specification.UserAccountSpecification;

import jakarta.transaction.Transactional;

@Service
public class UserAccountServiceImpl implements UserAccountService {
	
	private UserAccountRepository userAccountRepository;

	public UserAccountServiceImpl(UserAccountRepository userAccountRepository) {
		this.userAccountRepository = userAccountRepository;
	}

	@Override
	public UserAccount findUserAccountById(String userAccountId) {
		return userAccountRepository.findById(userAccountId).orElse(null);
	}

	@Override
	public Page<UserAccount> findUserAccountByUsernameOrEmailOrPhoneNumber(String keyWord, String orderBy, String sortBy, int page, int size) {
		List<String> allowedFields = List.of("username", "email", "phoneNumber");
		if (!allowedFields.contains(sortBy)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Thuộc tính cần sắp xếp không hợp lệ vui lòng thử lại");
		}

		Sort sort = orderBy.equalsIgnoreCase("asc") ? Sort.by(Sort.Direction.ASC, sortBy)
				: Sort.by(Sort.Direction.DESC, sortBy);

		Pageable pageable = PageRequest.of(page, size, sort);
		
		Specification<UserAccount> spec = Specification.where(UserAccountSpecification.equalsUsername(keyWord))
				.or(UserAccountSpecification.hasEmail(keyWord))
				.or(UserAccountSpecification.hasPhoneNumber(keyWord));
		
		return userAccountRepository.findAll(spec, pageable);
	}
	
	@Override
	public UserAccount findUserAccountByUsername(String username) {
		Specification<UserAccount> spec = UserAccountSpecification.equalsUsername(username);
		return userAccountRepository.findOne(spec).orElse(null);
	}

	@Transactional
	@Override
	public UserAccount disableUserAccount(String userAccountId) {
		UserAccount user = userAccountRepository.findById(userAccountId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Người dùng không tồn tại"
        ));
		user.setIsActive(false);
		return userAccountRepository.save(user);
	}

	@Transactional
	@Override
	public UserAccount activeUserAccount(String userAccountId) {
		UserAccount user = userAccountRepository.findById(userAccountId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Người dùng không tồn tại"
        ));
		user.setIsActive(true);
		return userAccountRepository.save(user);
	}

	@Override
	public Page<UserAccount> getUserAccounts(String orderBy, String sortBy, int page, int size) {
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

	@Override
	public UserAccount updateUserAccount(UserAccount userAccount) {
		return userAccountRepository.save(userAccount);
	}


}
