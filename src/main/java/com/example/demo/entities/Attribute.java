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
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
@Table(name = "attribute")
public class Attribute {

	@Id
	@Column(name = "id")
	@JsonView(View.Public.class)
	private String id;

	@Column(name = "name", nullable = false, unique = true)
	@JsonView(View.Public.class)
	private String name;
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY ,mappedBy = "attributes", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	private List<Category> categories = new ArrayList<>();

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "attribute")
	private List<AttributeValue> attributeValues = new ArrayList<>();

	@Column(name = "created_at", nullable = false)
	@JsonView(View.Internal.class)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	@JsonView(View.Internal.class)
	private LocalDateTime updatedAt;

	@PrePersist
	public void onCreate() {
		this.id = UUID.randomUUID().toString();
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

}
