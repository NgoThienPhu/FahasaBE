package com.example.demo.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.demo.util.view.View;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
	@Column(name = "product_image_id")
	@JsonView(View.Public.class)
	private String productImageId;

	@Column(name = "url", nullable = false)
	@JsonView(View.Public.class)
	private String url;

	@Column(name = "is_primary", nullable = false)
	@JsonView(View.Public.class)
	private Boolean isPrimary;

	@Column(name = "created_at", nullable = false)
	@JsonView(View.Employee.class)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	@JsonView(View.Employee.class)
	private LocalDateTime updatedAt;

	public ProductImage(String url, Boolean isPrimary) {
		this.url = url;
		this.isPrimary = isPrimary;
	}

	@PrePersist
	public void onCreate() {
		this.productImageId = UUID.randomUUID().toString();
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	public void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

	public static String extractFileNameFromUrl(String fileURL) {
		if (fileURL == null || !fileURL.contains("/"))
			return null;
		return fileURL.substring(fileURL.lastIndexOf("/") + 1);
	}

}
