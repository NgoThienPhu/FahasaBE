package com.example.demo.entities;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.entities.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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

	@JsonIgnore
	@ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinTable(name = "category_attribute", joinColumns = @JoinColumn(name = "category_id"), inverseJoinColumns = @JoinColumn(name = "attribute_id"))
	private List<Attribute> attributes = new ArrayList<Attribute>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Category> children = new ArrayList<Category>();

	public Category(String name) {
		this.name = name;
	}

}
