package com.example.demo.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateProductRequestDTO(

		@NotBlank(message = "Tên sản phẩm không được để trống") 
		String name,

		@NotBlank(message = "Mô tả sản phẩm không được để trống") 
		String description,

		@NotNull(message = "Mã loại sản phẩm không được để trống") 
		String categoryId,
		
		@NotNull(message = "Giá sản phẩm không được để trống")
		@Min(message = "Giá sản phẩm tối thiểu là 1.000 VND", value = 1000)
		Double price,
		
		@NotNull(message = "Số lượng sản phẩm không được để trống")
		@Min(message = "Số lượng sản phẩm tối thiểu là 1", value = 1)
		Integer quantity,
		
		@Valid
		List<CreateProductAttributeValueRequestDTO> attributes 
) {}
