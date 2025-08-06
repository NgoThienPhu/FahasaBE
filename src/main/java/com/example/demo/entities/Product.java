package com.example.demo.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.demo.entities.common.BaseEntity;
import com.example.demo.utils.view.View;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
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
public class Product extends BaseEntity {

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

	@Column(name = "sku_code")
	@JsonView(View.Public.class)
	private String skuCode;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JsonView(View.Public.class)
	private List<ProductImage> images = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JsonView(View.Public.class)
	private List<AttributeValue> attributeValues = new ArrayList<>();

	public Product(String name, String description, Category category, List<ProductImage> images,
			List<AttributeValue> attributeValues) {
		this.name = name;
		this.description = description;
		this.category = category;
		this.images = images;
		this.attributeValues = attributeValues;
	}

	@PrePersist
	public void onCreate() {
		super.onCreate();
		this.skuCode = UUID.randomUUID().toString();
	}

}
