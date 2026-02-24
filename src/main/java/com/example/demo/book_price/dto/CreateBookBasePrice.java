package com.example.demo.book_price.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;

public record CreateBookBasePrice(
	
	@DecimalMin(value = "1000", message = "Giá phải lớn hơn hoặc bằng 1000")
	BigDecimal price,
	
	@FutureOrPresent(message = "Ngày hiệu lực phải là ngày hiện tại hoặc trong tương lai")
	LocalDateTime effectiveFrom
	
) {}
