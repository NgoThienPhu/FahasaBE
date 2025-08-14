package com.example.demo.product.dto;

import java.math.BigDecimal;
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

		@NotBlank(message = "Mã loại sản phẩm không được để trống") 
		String categoryId,
		
		@NotNull(message = "Giá bán của sản phẩm không được để trống")
		@Min(message = "Giá bán của sản phẩm tối thiểu là 1.000 VND", value = 1000)
		BigDecimal sellPrice,
		
		@Min(message = "Giá nhập của sản phẩm tối thiểu là 1.000 VND", value = 1000)
		BigDecimal purchasePrice,
		
		@Valid
		List<String> attributeValues
) {}
