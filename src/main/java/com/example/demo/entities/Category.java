package com.example.demo.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.demo.util.view.View;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name = "category")
public class Category {

	@Id
	@Column(name = "id")
	@JsonView(View.Public.class)
	private String id;

	@Column(name = "name", nullable = false, unique = true)
	@JsonView(View.Public.class)
	private String name;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "parent_category_id", nullable = true)
	private Category parentCategory;

	@JsonIgnore
	@ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinTable(name = "category_attribute", joinColumns = @JoinColumn(name = "category_id"), inverseJoinColumns = @JoinColumn(name = "attribute_id"))
	private List<Attribute> attributes = new ArrayList<Attribute>();

	@OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonView(View.Public.class)
	private List<Category> children = new ArrayList<Category>();

	@Column(name = "created_at", nullable = false)
	@JsonView(View.Employee.class)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	@JsonView(View.Employee.class)
	private LocalDateTime updatedAt;

	public Category(String name, Category parentCategory) {
		this(name);
		this.parentCategory = parentCategory;
	}

	public Category(String name) {
		this.name = name;
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

}
