package com.example.demo.book_price.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CreateImportPriceDTO extends CreatePriceDTO {

	public CreateImportPriceDTO() {
		
	}

	public CreateImportPriceDTO(BigDecimal amount, LocalDateTime from) {
		super(amount, from);
	}
	
}
