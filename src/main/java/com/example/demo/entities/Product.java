package com.example.demo.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.demo.util.view.View;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
	@Column(name = "product_id")
	@JsonView(View.Public.class)
	private String productId;

	@Column(name = "name", nullable = false)
	@JsonView(View.Public.class)
	private String name;

	@Column(name = "description", nullable = false)
	@JsonView(View.Public.class)
	private String description;

	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false)
	@JsonView(View.Public.class)
	private Category category;

	@Column(name = "quantity", nullable = false)
	@JsonView(View.Public.class)
	private Integer quantity;

	@Column(name = "sku_code")
	@JsonView(View.Public.class)
	private String skuCode;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JsonView(View.Public.class)
	private List<ProductImage> images = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JsonView(View.Public.class)
	private List<AttributeValue> attributeValues = new ArrayList<>();

	@Column(name = "created_at", nullable = false)
	@JsonView(View.Employee.class)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	@JsonView(View.Employee.class)
	private LocalDateTime updatedAt;

	public Product(String name, String description, Category category, Integer quantity,
			List<ProductImage> images, List<AttributeValue> attributeValues) {
		this.name = name;
		this.description = description;
		this.category = category;
		this.quantity = quantity;
		this.images = images;
		this.attributeValues = attributeValues;
	}

	@PrePersist
	public void onCreate() {
		this.productId = UUID.randomUUID().toString();
		this.skuCode = UUID.randomUUID().toString();
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

}
