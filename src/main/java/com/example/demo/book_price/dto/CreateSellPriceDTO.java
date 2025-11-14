package com.example.demo.book_price.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CreateSellPriceDTO extends CreateImportPriceDTO {

	public CreateSellPriceDTO() {

	}

	public CreateSellPriceDTO(BigDecimal amount, LocalDateTime from) {
		super(amount, from);
	}
	
	

}
