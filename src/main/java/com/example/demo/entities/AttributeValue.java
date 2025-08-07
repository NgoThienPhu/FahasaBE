package com.example.demo.entities;

import com.example.demo.entities.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "attribute_value")
public class AttributeValue extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "attribute_id", nullable = false)
	private Attribute attribute;

	@Column(name = "value", nullable = false)
	private String value;

}
