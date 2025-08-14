package com.example.demo.price.entity;

import java.math.BigDecimal;

import com.example.demo.product.entity.Product;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@DiscriminatorValue(value = "SELL")
public class SellPrice extends ProductPrice {

	public SellPrice(Product product, BigDecimal price) {
		this.product = product;
		this.price = price;
		this.endDate = null;
	}
	
}
