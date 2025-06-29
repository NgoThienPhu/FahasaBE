package com.example.demo.entities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "description", nullable = false)
	private String description;

	@OneToOne
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	@Column(name = "price", nullable = false)
	private Double price;

	@Column(name = "quantity", nullable = false)
	private Integer quantity;

	@Column(name = "sku_code")
	private String skuCode;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "product")
	private List<ProductImage> images;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	public Product(String name, String description, Category category, Double price, Integer quantity,
			List<ProductImage> images) {
		this.name = name;
		this.description = description;
		this.category = category;
		this.price = price;
		this.quantity = quantity;
		this.images = images;
	}

	@PrePersist
	public void onCreate() {
		this.id = UUID.randomUUID().toString();
		this.skuCode = UUID.randomUUID().toString();
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

}
