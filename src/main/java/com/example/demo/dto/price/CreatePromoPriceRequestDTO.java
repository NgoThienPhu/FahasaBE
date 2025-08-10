package com.example.demo.dto.price;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePromoPriceRequestDTO(
	
	@NotBlank(message = "Tên khuyến mại không được để trống")
	String promoName,
	
	@NotNull(message = "Giá khuyến mại không được để trống")
    @DecimalMin(value = "1000.0", inclusive = false, message = "Giá khuyến mại phải lớn hơn 0")
	BigDecimal promoPrice,
	
	@NotNull(message = "Ngày bắt đầu không được để trống")
    @FutureOrPresent(message = "Ngày bắt đầu phải là hiện tại hoặc tương lai")
	LocalDateTime startDate,
	
	@NotNull(message = "Ngày kết thúc không được để trống")
	LocalDateTime endDate
		
) {}
