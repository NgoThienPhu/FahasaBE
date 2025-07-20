package com.example.demo.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "product_image")
public class ProductImage {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "url", nullable = false)
	private String url;

	@Column(name = "is_primary", nullable = false)
	private Boolean isPrimary;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name  = "product_id", nullable = false)
	private Product product;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at", nullable =  false)
	private LocalDateTime updatedAt;
	
	public ProductImage(String url, Boolean isPrimary) {
		this.url = url;
		this.isPrimary = isPrimary;
	}
	
	public ProductImage(String url, Boolean isPrimary, Product product) {
		this(url, isPrimary);
		this.product = product;
	}
	
	@PrePersist
	public void onCreate() {
		this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
	
	@PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
	
	public static String extractFileNameFromUrl(String fileURL) {
		if (fileURL == null || !fileURL.contains("/")) return null;
	    return fileURL.substring(fileURL.lastIndexOf("/") + 1);
	}

}
