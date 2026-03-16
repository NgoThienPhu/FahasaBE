package com.example.demo.account.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.demo.account.validation.validator.UniquePhoneNumberValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniquePhoneNumberValidator.class)
@Documented
public @interface UniquePhoneNumber {
	
	String message() default "PhoneNumber đã tồn tại";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
