package com.example.demo.price.entity;

import java.math.BigDecimal;

import com.example.demo.product.entity.Product;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@DiscriminatorValue(value = "PURCHASE")
public class PurchasePrice extends ProductPrice {
	
	public PurchasePrice(Product product, BigDecimal price) {
		this.product = product;
		this.price = price;
		this.endDate = null;
	}
	
}
