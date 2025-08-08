package com.example.demo.entities;

import com.example.demo.entities.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class ProductImage extends BaseEntity {

	@Column(name = "url", nullable = false)
	private String url;

	@Column(name = "is_primary", nullable = false)
	private Boolean isPrimary;
	
	public static String extractFileNameFromUrl(String fileURL) {
		if (fileURL == null || !fileURL.contains("/"))
			return null;
		return fileURL.substring(fileURL.lastIndexOf("/") + 1);
	}

}
