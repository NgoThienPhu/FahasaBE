package com.example.demo.dto.price;

import com.example.demo.entities.price.PromoPrice;
import com.example.demo.entities.price.SellPrice;
import com.fasterxml.jackson.annotation.JsonInclude;

//Ẩn đi những thuộc tính có giá trị là null
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductPriceResponseDTO(

		SellPrice sellPrice,
		
		PromoPrice promoPrice
		
) {}
