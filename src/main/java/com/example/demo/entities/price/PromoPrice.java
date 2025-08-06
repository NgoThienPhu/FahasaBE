package com.example.demo.entities.price;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entities.Product;
import com.example.demo.util.view.View;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;

@Entity
@DiscriminatorValue(value = "PROMO")
public class PromoPrice extends ProductPrice {
	
	@Column(name = "promo_name", nullable = false)
	@JsonView(View.Employee.class)
	private String promoName;

	public PromoPrice(Product product, String promoName, BigDecimal price, LocalDateTime endDate) {
		this.product = product;
		this.promoName = promoName;
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
