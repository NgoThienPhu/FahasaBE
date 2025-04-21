package com.example.demo.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "image")
public class Image {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "url", nullable = false)
	private String url;

	@Column(name = "alt_text", nullable = false)
	private String altText;
	
	@ManyToOne
	@JoinColumn(name  = "product_variant", nullable = false)
	private ProductVariant productVariant;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at", nullable =  false)
	private LocalDateTime updatedAt;
}
