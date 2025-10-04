package com.example.demo.email.application;

import com.example.demo.email.entity.Email;

public interface EmailApplicationService {

	Boolean exists(String email);

	Email verify(String email, String otp);

}
