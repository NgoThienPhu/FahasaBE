package com.example.demo.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name = "category")
public class Category {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@OneToOne
	@JoinColumn(name = "parent_category_id", nullable = true)
	private Category category;

	@Column(name = "level", nullable = false)
	private Integer level;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;
	
	public Category(String name, Category category, Integer level) {
		this(name, category);
		this.level = level;
	}

	public Category(String name, Category category) {
		this(name);
		this.category = category;
	}

	public Category(String name) {
		this.name = name;
	}

	@PrePersist
	public void onCreate() {
		this.id = UUID.randomUUID().toString();
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
		this.level = (category == null) ? 0 : this.category.level + 1;
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

}
