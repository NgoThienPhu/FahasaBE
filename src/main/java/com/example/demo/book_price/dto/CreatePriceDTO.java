package com.example.demo.book_price.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class CreatePriceDTO {
	
	@Min(value = 1_000, message = "Giá sản phẩm tối thiểu là 1.000 VNĐ")
	protected BigDecimal amount;
	
	@FutureOrPresent(message = "Thời gian bắt đầu phải hớn hơn hoặc bằng thời gian hiện tại")
	protected LocalDateTime from;

}
