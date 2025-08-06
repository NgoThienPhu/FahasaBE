package com.example.demo.entities.common;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.demo.entities.Product;
import com.example.demo.utils.view.View;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "price_type")
@Table(name = "product_price")
public abstract class ProductPrice {
	
	@Id
	protected String id;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	protected Product product;
	
	@Column(name = "price", nullable = false)
	@JsonView(View.Public.class)
	protected BigDecimal price;
	
	@Column(name = "start_date", nullable = false, updatable = false)
	@JsonView(View.Employee.class)
	protected LocalDateTime startDate;
	
	@Column(name = "end_date", nullable = true)
	@JsonView(View.Employee.class)
	protected LocalDateTime endDate;
	
	@PrePersist
    protected void onCreate() {
		this.id = UUID.randomUUID().toString();
        this.startDate = LocalDateTime.now();
    }

}
