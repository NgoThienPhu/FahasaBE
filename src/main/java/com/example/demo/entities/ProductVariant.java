package com.example.demo.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "product_variant")
public class ProductVariant {
	
	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;
	
	@Column(name = "sku_code", nullable = false)
	private String skuCode;
	
	@Column(name = "price", nullable = false)
	private double price;
	
	@Column(name = "quantity", nullable = false)
	private int quantity;
	
	@OneToOne
	@JoinColumn(name = "image_id", nullable = false)
	private Image image;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "productVariant")
	private List<Image> images;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<ProductAttributeValue> productAttributeValue = new ArrayList<>();
	
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;
	
	@PrePersist
	public void onCreate() {
		this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
	
	@PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
