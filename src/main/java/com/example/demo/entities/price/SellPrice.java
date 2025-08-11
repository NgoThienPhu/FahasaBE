package com.example.demo.entities.price;

import java.math.BigDecimal;

import com.example.demo.entities.Product;
import com.example.demo.entities.base.ProductPrice;

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
