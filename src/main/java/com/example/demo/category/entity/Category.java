package com.example.demo.category.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.attribute.entity.Attribute;
import com.example.demo.common.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class Category extends BaseEntity {

	@Column(name = "name", nullable = false, unique = true)
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "parent_id")
	private Category parent;
	
	@JsonIgnore
	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Category> children = new ArrayList<>();
	
	@JsonIgnore
	@ManyToMany
	@JoinTable(
			name = "category_attribute",
			joinColumns = @JoinColumn(name = "category_id"),
			inverseJoinColumns = @JoinColumn(name = "attribute_id")
	)
	private List<Attribute> attributes = new ArrayList<>();

	public Category(String name) {
		this.name = name;
	}

}
