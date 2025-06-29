package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record ProductCategoryRequestDTO(

		String categoryId,

		@NotBlank(message = "Tên loại sản phẩm không được để trống") String categoryName) {
}
