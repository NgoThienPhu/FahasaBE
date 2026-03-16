package com.example.demo.util.validation;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.example.demo.util.exception.CustomException;

@Component
public class SortValidator {
	
	public static void validate(Set<String> allowedFields, String sortBy) {
		
		if (!allowedFields.contains(sortBy)) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Thuộc tính cần sắp xếp không hợp lệ vui lòng thử lại");
		}
		
	}

}
