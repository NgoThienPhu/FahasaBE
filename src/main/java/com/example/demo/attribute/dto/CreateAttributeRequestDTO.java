package com.example.demo.attribute.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateAttributeRequestDTO(

		@NotBlank(message = "Tên thuộc tính không được để trống") 
		String attributeName

) {
}
