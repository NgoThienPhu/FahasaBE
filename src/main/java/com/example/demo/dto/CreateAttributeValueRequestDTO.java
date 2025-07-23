package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateAttributeValueRequestDTO(
		
		String attributeId,

		@NotBlank(message = "Tên thuộc tính không được để trống") 
		String attributeName,

		@NotBlank(message = "Giá trị thuộc tính không được để trống") 
		String attributeValue

) {
}
