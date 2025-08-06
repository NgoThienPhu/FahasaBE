package com.example.demo.entities.price;

import java.math.BigDecimal;

import com.example.demo.entities.Product;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue(value = "PURCHASE")
public class PurchasePrice extends ProductPrice {
	
	public PurchasePrice(Product product, BigDecimal price) {
		this.product = product;
		this.price = price;
		this.endDate = null;
	}
	
}
