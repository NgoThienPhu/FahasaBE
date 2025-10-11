package com.example.demo.category.entity;

import com.example.demo.util.base.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "category")
public class Category extends BaseEntity {

	@Column(name = "name", nullable = false)
	private String name;
	
	public Category(String name) {
		this.name = name;
	}

}
