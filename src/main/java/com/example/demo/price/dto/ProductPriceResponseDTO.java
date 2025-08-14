package com.example.demo.price.dto;

import com.example.demo.price.entity.SellPrice;
import com.fasterxml.jackson.annotation.JsonInclude;

//Ẩn đi những thuộc tính có giá trị là null
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductPriceResponseDTO(

		SellPrice sellPrice,
		
		PromoPriceResponseDTO promoPrice
		
) {}
