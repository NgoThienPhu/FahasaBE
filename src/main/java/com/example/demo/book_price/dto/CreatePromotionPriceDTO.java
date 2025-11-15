package com.example.demo.book_price.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePromotionPriceDTO extends CreatePriceDTO {
	
	private LocalDateTime to;

	public CreatePromotionPriceDTO(BigDecimal amount, LocalDateTime from, LocalDateTime to) {
		super(amount, from);
		this.to = to;
	}
	
}
