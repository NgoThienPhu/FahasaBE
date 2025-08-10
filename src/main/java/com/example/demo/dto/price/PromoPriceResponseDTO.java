package com.example.demo.dto.price;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.demo.entities.price.PromoPrice;

public record PromoPriceResponseDTO(
	    String id,
	    String promoName,
	    BigDecimal promoPrice,
	    LocalDateTime startDate,
	    LocalDateTime endDate,
	    PromoPrice.ManualStatus manualStatus,
	    PromoPrice.PromoState currentState
	) {

	    public static PromoPriceResponseDTO fromEntity(PromoPrice promo) {
	    	
	    	if(promo == null) return null;
	    	
	        return new PromoPriceResponseDTO(
	            promo.getId(),
	            promo.getPromoName(),
	            promo.getPrice(),
	            promo.getStartDate(),
	            promo.getEndDate(),
	            promo.getManualStatus(),
	            promo.getCurrentState()
	        );
	        
	    }
	}
