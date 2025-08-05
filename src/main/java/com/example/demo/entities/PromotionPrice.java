package com.example.demo.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entities.bases.ProductPrice;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;

@Entity
@DiscriminatorValue(value = "PROMOTION")
public class PromotionPrice extends ProductPrice {

	public PromotionPrice(Product product, BigDecimal price, LocalDateTime endDate) {
		this.product = product;
		this.price = price;
		this.endDate = endDate;
	}

	@PrePersist
	public void onCreate() {

		if (endDate == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ngày kế thúc giá khuyến mại là bắt buộc");

		if (endDate.isBefore(this.startDate))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Ngày kế thúc giá khuyến mại phải sau ngày bắt đầu");
		
	}

}
