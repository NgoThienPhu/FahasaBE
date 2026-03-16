package com.example.demo.account.validation.validator;

import com.example.demo.account.repository.AccountRepository;
import com.example.demo.account.validation.annotation.UniqueUsername;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {
	
	private AccountRepository accountRepository;
	
	public UniqueUsernameValidator(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public boolean isValid(String username, ConstraintValidatorContext context) {
		if (username == null) {
            return true;
        }

        return !accountRepository.existsByUsername(username);
	}

}
