package com.example.demo.email.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.email.entity.Email;
import com.example.demo.email.repository.EmailRepository;
import com.example.demo.email.service.EmailService;
import com.example.demo.email.specification.EmailSpecification;

@Service
public class EmailServiceImpl implements EmailService {

	private EmailRepository emailRepository;

	public EmailServiceImpl(EmailRepository emailRepository) {
		this.emailRepository = emailRepository;
	}

	@Override
	public Email verify(String email) {
		Email myEmail = emailRepository.findOne(EmailSpecification.hasEmail(email))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						String.format("Không tìm thấy email: %s", email)));
		myEmail.verify();
		return emailRepository.save(myEmail);
	}

	@Override
	public Boolean exists(String email) {
		return emailRepository.count(EmailSpecification.hasEmail(email)) > 0;
	}

}
