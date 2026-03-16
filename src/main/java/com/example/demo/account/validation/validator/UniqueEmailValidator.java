package com.example.demo.account.validation.validator;

import com.example.demo.account.repository.AccountRepository;
import com.example.demo.account.validation.annotation.UniqueEmail;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
	
	private AccountRepository accountRepository;
	
	public UniqueEmailValidator(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		if (email == null) {
            return true;
        }

        return !accountRepository.existsByEmail(email);
	}

}
