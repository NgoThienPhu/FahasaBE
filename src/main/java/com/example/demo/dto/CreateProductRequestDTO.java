package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateProductRequestDTO(

		@NotBlank(message = "Tên sản phẩm không được để trống") 
		String name,

		@NotBlank(message = "Mô tả sản phẩm không được để trống") 
		String description,

		@NotBlank(message = "Mã loại sản phẩm không được để trống") 
		String categoryId,
		
		@Min(message = "Giá sản phẩm tối thiểu là 1.000 VND", value = 1000)
		BigDecimal price,
		
		@Min(message = "Số lượng sản phẩm tối thiểu là 1", value = 1)
		Integer quantity,
		
		@Valid
		List<CreateAttributeValueRequestDTO> attributes 
) {}
