package com.example.demo.entities;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.entities.common.BaseEntity;
import com.example.demo.utils.view.View;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
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
@Table(name = "attribute")
public class Attribute extends BaseEntity {

	@Column(name = "name", nullable = false, unique = true)
	@JsonView(View.Public.class)
	private String name;
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY ,mappedBy = "attributes", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	private List<Category> categories = new ArrayList<>();

}
