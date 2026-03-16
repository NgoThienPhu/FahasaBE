package com.example.demo.account.validation.validator;

import com.example.demo.account.repository.UserAccountRepository;
import com.example.demo.account.validation.annotation.UniquePhoneNumber;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniquePhoneNumberValidator implements ConstraintValidator<UniquePhoneNumber, String> {

	private UserAccountRepository userAccountRepository;

	public UniquePhoneNumberValidator(UserAccountRepository userAccountRepository) {
		this.userAccountRepository = userAccountRepository;
	}

	@Override
	public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
		if (phoneNumber == null) {
			return true;
		}

		return !userAccountRepository.existsByPhoneNumber(phoneNumber);
	}

}
