package com.example.demo.validator;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryValidator {
	
	@NotBlank(message = "Id loại sản phẩm không được để trống")
	private String id;
	
	@NotBlank(message = "Tên loại sản phẩm không được để trống")
	private String name;
	
}
